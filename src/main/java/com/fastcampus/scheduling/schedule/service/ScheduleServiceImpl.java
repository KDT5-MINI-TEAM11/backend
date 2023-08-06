package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.exception.CustomException;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.AddScheduleDTO;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.time.LocalDate;
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
    public List<Schedule> findByYear(Long userId, LocalDate startDate, LocalDate endDate) {

        List<Schedule> allSchedules = scheduleRepository.findByUserIdAndYear(userId, startDate, endDate);

        return allSchedules;
    }

    @Transactional
    public Schedule addSchedule(AddScheduleDTO addScheduleDTO) {

        User user = userRepository.findById(addScheduleDTO.getUserId())
            .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

        LocalDate startDate = addScheduleDTO.getStartDate();
        LocalDate endDate = addScheduleDTO.getEndDate();

        if (startDate.isAfter(endDate)) {
            throw new Exception400(ErrorMessage.INVALID_CHANGE_POSITION);
        }

        Schedule schedule = Schedule.builder()
            .user(user)
            .scheduleType(addScheduleDTO.getScheduleType())
            .startDate(addScheduleDTO.getStartDate())
            .endDate(addScheduleDTO.getEndDate())
            .state(State.PENDING)
            .build();

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void cancelSchedule(Long id, Long userId) throws CustomException {
        Schedule schedule = scheduleRepository.findByIdAndUserId(id, userId);

        if (schedule != null && schedule.getState() == State.PENDING) {
            scheduleRepository.delete(schedule);
        } else {
            throw new Exception400(ErrorMessage.CANNOT_CANCEL_SCHEDULE);
        }
    }

    @Transactional
    public Schedule modifySchedule(Long id, LocalDate startDate, LocalDate endDate) {

        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

        if (startDate.isAfter(endDate)) {
            throw new Exception400(ErrorMessage.INVALID_CHANGE_POSITION);
        }

        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule getScheduleByIdAndUserId(Long id, Long userId) {
        return scheduleRepository.findByIdAndUserId(id, userId);
    }

    @Transactional
    public List<Schedule> findAllByYear(LocalDate startDate, LocalDate endDate) {

        return scheduleRepository.findAllByYear(startDate, endDate);
    }

}
