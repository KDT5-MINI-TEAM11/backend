package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling.schedule.dto.ScheduleResponse;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    Schedule getScheduleById(Long userId);

    Schedule addSchedule(ScheduleResponse.AddScheduleDTO addScheduleDTO);

    void cancelSchedule(Long userId);

    Schedule modifySchedule(Long id, LocalDate startDate, LocalDate endDate);

    List<Schedule> getAllSchedulesByUserId(Long userId);

    List<Schedule> getSchedulesByYearAndMonth(int year, int month);

}
