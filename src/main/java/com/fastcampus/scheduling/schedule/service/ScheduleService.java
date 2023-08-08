package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling.schedule.dto.ScheduleRequest.ModifyScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    Schedule addSchedule(ScheduleResponse.AddScheduleDTO addScheduleDTO);

    void cancelSchedule(Long id, Long userId);

    List<Schedule> findAllByYear(LocalDate startDate, LocalDate endDate);

    Schedule modifySchedule(ModifyScheduleDTO modifyScheduleDTO, Long userId);
}
