package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling.schedule.dto.ScheduleRequest;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {

    Schedule addSchedule(ScheduleRequest.AddScheduleDTO addScheduleDTO);

    void cancelSchedule(Long id, Long userId);

    List<Schedule> findAllByYear(LocalDateTime startDate, LocalDateTime endDate);

    Schedule modifySchedule(ScheduleRequest.ModifyScheduleDTO modifyScheduleDTO, Long userId);
}
