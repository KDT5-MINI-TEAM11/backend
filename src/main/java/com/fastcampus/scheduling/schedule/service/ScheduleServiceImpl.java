package com.fastcampus.scheduling.schedule.service;

import com.fastcampus.scheduling.schedule.dto.ScheduleRequest.ModifyScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.AddScheduleDTO;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    public List<Schedule> getScheduleById(Long userId) {
        return scheduleRepository.findByScheduleId(userId);

    }

    @Override
    public Schedule addSchedule(AddScheduleDTO addScheduleDTO) {
        Schedule schedule = Schedule.builder()
            .scheduleType(addScheduleDTO.getScheduleType())
            .startDate(addScheduleDTO.getStartDate())
            .endDate(addScheduleDTO.getEndDate())
            .state(addScheduleDTO.getState())
            .build();

        return scheduleRepository.save(schedule);
    }

    @Override
    public void cancelSchedule(Long id) {

    }

    @Override
    public Schedule modifySchedule(Long id, ModifyScheduleDTO modifyScheduleDTO) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        if (schedule != null) {
            // Update fields from DTO to entity
            // ...

            return scheduleRepository.save(schedule);
        }
        return null;
    }

}
