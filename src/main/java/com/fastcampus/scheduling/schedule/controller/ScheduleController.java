package com.fastcampus.scheduling.schedule.controller;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import com.fastcampus.scheduling.schedule.dto.ScheduleRequest.ModifyScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.AddScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.GetUserScheduleDTO;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.service.ScheduleServiceImpl;
import com.fastcampus.scheduling.user.service.UserService;
import java.time.format.DateTimeFormatter;
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
    private final UserService userService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @GetMapping("/user/schedule")
    public ResponseEntity<ApiResponse.Result<ScheduleResponse.GetUserScheduleDTO>> getSchedule() {
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
    public Schedule modifySchedule(@PathVariable Long id, @RequestBody ModifyScheduleDTO modifyScheduleDTO) {
        return scheduleServiceImpl.modifySchedule(id, modifyScheduleDTO);
    }



}
