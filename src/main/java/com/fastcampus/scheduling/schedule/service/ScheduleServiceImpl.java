package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.AddScheduleDTO;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule getScheduleById(Long userId) {
        return scheduleRepository.findById(userId)
            .orElseThrow(() -> new Exception401(
                ErrorMessage.USER_NOT_FOUND));
    }

    @Transactional
    public Schedule addSchedule(AddScheduleDTO addScheduleDTO) {
        Schedule schedule = Schedule.builder()
            .scheduleType(addScheduleDTO.getScheduleType())
            .startDate(addScheduleDTO.getStartDate())
            .endDate(addScheduleDTO.getEndDate())
            .state(addScheduleDTO.getState())
            .build();

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void cancelSchedule(Long id) {

    }

    @Transactional
    public Schedule modifySchedule(Long id, LocalDate startDate, LocalDate endDate) {

        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);

        return scheduleRepository.save(schedule);
    }

}
