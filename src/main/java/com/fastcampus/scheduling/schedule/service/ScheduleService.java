package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    Schedule getScheduleById(Long userId);

    Schedule addSchedule(ScheduleResponse.AddScheduleDTO addScheduleDTO);

    void cancelSchedule(Long id, Long userId);

    Schedule modifySchedule(Long id, LocalDate startDate, LocalDate endDate);

    List<Schedule> getAllSchedulesByUserIdAndDate(Long userId, LocalDate startDate, LocalDate endDate);

    List<Schedule> findByUserId(Long userId);

    List<Schedule> getSchedulesBetweenDates(State state, LocalDate startDate, LocalDate endDate);

    List<Schedule> findByUserId(Long userId);

}
