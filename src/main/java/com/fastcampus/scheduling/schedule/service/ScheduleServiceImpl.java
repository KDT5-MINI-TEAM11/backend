package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.exception.CustomException;
import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.dto.ScheduleRequest;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Schedule> findByYear(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Schedule> allSchedules = scheduleRepository.findSchedulesByUserIdAndStartDateBetween(userId, startDate, endDate);

        return allSchedules;
    }

    @Transactional
    public Schedule addSchedule(ScheduleRequest.AddScheduleDTO addScheduleDTO) {
        User user = userRepository.findById(addScheduleDTO.getUserId())
            .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

        validateStartDate(addScheduleDTO.getStartDate());

        if (addScheduleDTO.getScheduleType() == ScheduleType.ANNUAL) {
            LocalDateTime startDate = LocalDateTime.of(addScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0));
            LocalDateTime endDate = LocalDateTime.of(addScheduleDTO.getEndDate(), LocalTime.MAX.withNano(0));

            if (startDate.isAfter(endDate)) {
                throw new Exception400(ErrorMessage.INVALID_CHANGE_POSITION);
            }
            if (isScheduleAddOverlap(addScheduleDTO)) {
                throw new Exception400(ErrorMessage.OVERLAPPING_SCHEDULE);
            }

            int requestedVacationDays = calculateDuration(startDate, endDate);
            int remainingVacation = user.getPosition().getTotalVacation() - user.getUsedVacation();

            if (requestedVacationDays > remainingVacation) {
                throw new Exception400(ErrorMessage.INSUFFICIENT_VACATION_DAYS);
            }
            user.setUsedVacation(user.getUsedVacation() + requestedVacationDays);

        } else if (addScheduleDTO.getScheduleType() == ScheduleType.DUTY) {
            LocalDateTime startDate = LocalDateTime.of(addScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0));
            addScheduleDTO.setEndDate(startDate.toLocalDate());

            if (isDutyScheduleOverlap(addScheduleDTO.getUserId(), startDate)) {
                throw new Exception400(ErrorMessage.OVERLAPPING_SCHEDULE);
            }

            Schedule schedule = Schedule.builder()
                .user(user)
                .scheduleType(addScheduleDTO.getScheduleType())
                .startDate(LocalDateTime.of(addScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0)))
                .endDate(LocalDateTime.of(addScheduleDTO.getEndDate(), LocalTime.MAX.withNano(0)))
                .state(State.PENDING)
                .build();

            return scheduleRepository.save(schedule);
        }
        Schedule schedule = Schedule.builder()
            .user(user)
            .scheduleType(addScheduleDTO.getScheduleType())
            .startDate(LocalDateTime.of(addScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0)))
            .endDate(LocalDateTime.of(addScheduleDTO.getEndDate(), LocalTime.MAX.withNano(0)))
            .state(State.PENDING)
            .build();

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void cancelSchedule(Long id, Long userId) throws CustomException {
        Schedule schedule = scheduleRepository.findByIdAndUserId(id, userId);

        if (schedule != null && schedule.getState() == State.PENDING || schedule.getState() == State.APPROVE) {
            LocalDateTime now = LocalDateTime.now();

            if (schedule.getStartDate().isBefore(now) && !schedule.getStartDate().toLocalDate().equals(now.toLocalDate())) {
                throw new Exception400(ErrorMessage.CANNOT_CANCEL_SCHEDULE);
            }

            int canceledVacationDays = calculateDuration(schedule.getStartDate(), schedule.getEndDate());
            User user = schedule.getUser();

            if (schedule.getScheduleType() == ScheduleType.ANNUAL) {
                user.setUsedVacation(user.getUsedVacation() - canceledVacationDays);
            }

            scheduleRepository.delete(schedule);
        } else {
            throw new Exception400(ErrorMessage.CANNOT_CANCEL_SCHEDULE);
        }
    }

    @Transactional
    public Schedule modifySchedule(ScheduleRequest.ModifyScheduleDTO modifyScheduleDTO, Long userId) {
        Schedule existingSchedule = getScheduleByIdAndUserId(modifyScheduleDTO.getId(), userId);

        validateStartDate(modifyScheduleDTO.getStartDate());

        if (existingSchedule.getState() != State.PENDING) {
            throw new Exception400(ErrorMessage.APPROVED_SCHEDULE);
        }

        if (existingSchedule.getScheduleType() == ScheduleType.ANNUAL) {
            if (isScheduleModifyOverlap(modifyScheduleDTO)) {
                throw new Exception400(ErrorMessage.OVERLAPPING_SCHEDULE);
            }

            LocalDateTime oldStartDate = existingSchedule.getStartDate();
            LocalDateTime oldEndDate = existingSchedule.getEndDate();
            int requestedVacationDays = calculateDuration(oldStartDate, oldEndDate);

            User user = existingSchedule.getUser();
            LocalDateTime newStartDate = LocalDateTime.of(modifyScheduleDTO.getStartDate(),
                LocalTime.MIN.withNano(0));
            LocalDateTime newEndDate = LocalDateTime.of(modifyScheduleDTO.getEndDate(),
                LocalTime.MAX.withNano(0));

            if (newStartDate.isAfter(newEndDate)) {
                throw new Exception400(ErrorMessage.INVALID_CHANGE_POSITION);
            }

            if (newStartDate != null || newEndDate != null) {
                int newVacationDays = calculateDuration(
                    newStartDate != null ? newStartDate : oldStartDate,
                    newEndDate != null ? newEndDate : oldEndDate);

                int remainingVacation = user.getPosition().getTotalVacation() - user.getUsedVacation();

                if (remainingVacation < newVacationDays) {
                    throw new Exception400(ErrorMessage.INSUFFICIENT_VACATION_DAYS);
                }
                user.setUsedVacation((user.getUsedVacation() - requestedVacationDays) + newVacationDays);
            }
            userRepository.save(user);
            if (newStartDate != null) {
                existingSchedule.setStartDate(newStartDate);
            }
            if (newEndDate != null) {
                existingSchedule.setEndDate(newEndDate);
            }
         }

        if (existingSchedule.getScheduleType() == ScheduleType.DUTY) {
            LocalDateTime newStartDate = LocalDateTime.of(modifyScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0));
            LocalDateTime newEndDate = LocalDateTime.of(modifyScheduleDTO.getStartDate(), LocalTime.MAX.withNano(0));

            if (isDutyScheduleOverlap(userId, newStartDate)) {
                throw new Exception400(ErrorMessage.OVERLAPPING_SCHEDULE);
            }

            existingSchedule.setStartDate(newStartDate);
            existingSchedule.setEndDate(newEndDate);
        }
        scheduleRepository.save(existingSchedule);
        return existingSchedule;
    }

    @Transactional
    public Schedule getScheduleByIdAndUserId(Long id, Long userId) {
        return scheduleRepository.findByIdAndUserId(id, userId);
    }

    @Transactional
    public List<Schedule> findAllByYear(LocalDateTime startDate, LocalDateTime endDate) {

        return scheduleRepository.findSchedulesByStartDateBetween(startDate, endDate);
    }

    public int calculateDuration(LocalDateTime startDate, LocalDateTime endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    private boolean isScheduleAddOverlap(ScheduleRequest.AddScheduleDTO addScheduleDTO) {
        List<Schedule> existingSchedules = scheduleRepository.findByUserAndDatesOverlap(
            addScheduleDTO.getUserId(),
            LocalDateTime.of(addScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0)),
            LocalDateTime.of(addScheduleDTO.getEndDate(), LocalTime.MAX.withNano(0))
        );

        for (Schedule existingSchedule : existingSchedules) {
            if (existingSchedule.getState() != State.REJECT &&
                existingSchedule.getStartDate().isBefore(LocalDateTime.of(addScheduleDTO.getEndDate(), LocalTime.MAX.withNano(0))) &&
                existingSchedule.getEndDate().isAfter(LocalDateTime.of(addScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0)))) {
                return true;
            }
        }
        return false;
    }

    private boolean isScheduleModifyOverlap(ScheduleRequest.ModifyScheduleDTO modifyScheduleDTO) {
        List<Schedule> existingSchedules = scheduleRepository.findByScheduleAndDatesOverlap(
            modifyScheduleDTO.getId(),
            LocalDateTime.of(modifyScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0)),
            LocalDateTime.of(modifyScheduleDTO.getEndDate(), LocalTime.MAX.withNano(0))
        );

        for (Schedule existingSchedule : existingSchedules) {
            if (existingSchedule.getState() != State.REJECT &&
                existingSchedule.getStartDate().isBefore(LocalDateTime.of(modifyScheduleDTO.getEndDate(), LocalTime.MAX.withNano(0))) &&
                existingSchedule.getEndDate().isAfter(LocalDateTime.of(modifyScheduleDTO.getStartDate(), LocalTime.MIN.withNano(0)))) {
                return true;
            }
        }
        return false;
    }

    private boolean isDutyScheduleOverlap(Long userId, LocalDateTime newStartDate) {
        List<Schedule> existingSchedules = scheduleRepository.findByUserAndDatesOverlap(
            userId,
            newStartDate,
            newStartDate.plusDays(1)
        );
        return !existingSchedules.isEmpty();
    }

    private void validateStartDate(LocalDate startDate) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime selectedStartDate = LocalDateTime.of(startDate, LocalTime.MAX.withNano(0));

        if (selectedStartDate.isBefore(currentDate)) {
            throw new Exception400(ErrorMessage.INVALID_CHANGE_POSITION);
        }
    }
}
