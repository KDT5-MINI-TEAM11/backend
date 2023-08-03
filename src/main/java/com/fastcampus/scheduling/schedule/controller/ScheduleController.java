package com.fastcampus.scheduling.schedule.controller;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import com.fastcampus.scheduling.schedule.dto.ScheduleRequest.ModifyScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.AddScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.GetUserScheduleDTO;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.service.ScheduleServiceImpl;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ScheduleController {

    private final ScheduleServiceImpl scheduleServiceImpl;

    @GetMapping("/user/schedule")
    public ResponseEntity<Result<ScheduleResponse.GetUserScheduleDTO>> getSchedule() {
        Long userId = Long.valueOf(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        Schedule schedule = scheduleServiceImpl.getScheduleById(userId);

        GetUserScheduleDTO getUserScheduleDTO = GetUserScheduleDTO.from(schedule);

        return ResponseEntity.ok(ApiResponse.success(getUserScheduleDTO));

    }

    @PostMapping("/user/schedule/add")
    public ResponseEntity<Result<AddScheduleDTO>>  addSchedule(@RequestBody AddScheduleDTO addScheduleDTO) {

        Schedule savedSchedule = scheduleServiceImpl.addSchedule(addScheduleDTO);

        AddScheduleDTO addSchedule = AddScheduleDTO.from(savedSchedule);

        return ResponseEntity.ok(ApiResponse.success(addSchedule));
    }

    @PostMapping("/user/schedule/cancel")
    public void cancelSchedule(@PathVariable Long id) {
        scheduleServiceImpl.cancelSchedule(id);
    }

    @PatchMapping("/user/schedule/modify")
    public ResponseEntity<Result<ModifyScheduleDTO>> modifySchedule(@RequestBody ModifyScheduleDTO modifyScheduleDTO) {

        Long userId = Long.valueOf(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        Schedule schedule = scheduleServiceImpl.getScheduleById(userId);

        LocalDate startDate = modifyScheduleDTO.getStartDate();
        if (startDate != null) {
            schedule.setStartDate(startDate);
        }

        LocalDate endDate = modifyScheduleDTO.getEndDate();
        if (endDate != null) {
            schedule.setEndDate(endDate);
        }

        Schedule modifySchedule = scheduleServiceImpl.modifySchedule(userId, schedule.getStartDate(), schedule.getEndDate());

        ModifyScheduleDTO modifyScheduleDTOs = ModifyScheduleDTO.from(modifySchedule);

        return ResponseEntity.ok(ApiResponse.success(modifyScheduleDTOs));
    }



}
