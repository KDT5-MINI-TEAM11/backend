package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception401;
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
    public List<Schedule> findByUserId(Long userId) {
        return scheduleRepository.findByUserId(userId);
    }

    @Transactional
    public List<Schedule> getAllSchedulesByUserIdAndDate(Long userId, LocalDate startDate) {

        List<Schedule> allSchedules = scheduleRepository.findByUserIdAndStartDateAfter(userId, startDate);

        return allSchedules;
    }

    @Transactional
    public Schedule getScheduleById(Long userId) {
        return scheduleRepository.findById(userId)
            .orElseThrow(() -> new Exception401(
                ErrorMessage.USER_NOT_FOUND));
    }

    @Transactional
    public Schedule addSchedule(AddScheduleDTO addScheduleDTO) {

        User user = userRepository.findById(addScheduleDTO.getUserId())
            .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

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
    public void cancelSchedule(Long id, Long userId) {

        Schedule schedule = scheduleRepository.findByIdAndUserId(id, userId);

        if (schedule != null) {
            String message = "일정(ID: " + id + ")이(가) 취소되었습니다.";
        }
    }

    @Transactional
    public Schedule modifySchedule(Long id, LocalDate startDate, LocalDate endDate) {

        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public List<Schedule> getSchedulesByYearAndMonth(int year, int month) {

        return scheduleRepository.findByStartDateYearAndStartDateMonth(year, month);
    }
}
