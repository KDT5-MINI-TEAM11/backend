package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling.schedule.dto.ScheduleRequest;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.util.List;

public interface ScheduleService {

    List<Schedule> getScheduleById(Long userId);

    Schedule addSchedule(ScheduleResponse.AddScheduleDTO addScheduleDTO);

    void cancelSchedule(Long userId);

    Schedule modifySchedule(Long id, ScheduleRequest.ModifyScheduleDTO modifyScheduleDTO);



}
