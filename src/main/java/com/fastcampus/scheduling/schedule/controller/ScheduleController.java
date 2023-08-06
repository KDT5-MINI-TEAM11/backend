package com.fastcampus.scheduling.schedule.controller;

import com.fastcampus.scheduling._core.exception.CustomException;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import com.fastcampus.scheduling.schedule.dto.ScheduleRequest.ModifyScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.AddScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.GetAllScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.GetUserScheduleDTO;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.service.ScheduleServiceImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ScheduleController {

    private final ScheduleServiceImpl scheduleServiceImpl;

    @GetMapping("/user/schedule")
    public ResponseEntity<Result<List<GetUserScheduleDTO>>> getUserSchedule(
        @RequestParam(name = "year", required = true) Integer year) {

        Long userId = Long.valueOf(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        List<Schedule> schedules;

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        schedules = scheduleServiceImpl.findByYear(userId, startDate, endDate);

        List<GetUserScheduleDTO> userSchedulesDTO = schedules.stream()
            .map(schedule -> GetUserScheduleDTO.from(schedule))
            .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(userSchedulesDTO));
    }

    @PostMapping("/user/schedule/add")
    public ResponseEntity<Result<AddScheduleDTO>> addSchedule(@RequestBody AddScheduleDTO addScheduleDTO) {

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        addScheduleDTO.setUserId(userId);

        Schedule savedSchedule = scheduleServiceImpl.addSchedule(addScheduleDTO);
        AddScheduleDTO addSchedule = AddScheduleDTO.from(savedSchedule);

        return ResponseEntity.ok(ApiResponse.success(addSchedule));
    }

    @PostMapping("/user/schedule/cancel")
    public ResponseEntity<Result> cancelSchedule(@RequestBody Map<String, String> request) throws CustomException {
        Long userId = Long.valueOf(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        Long id = Long.valueOf(request.get("id"));

        scheduleServiceImpl.cancelSchedule(id, userId);

        String message = "정상적으로 취소 되었습니다";
        return ResponseEntity.ok(ApiResponse.success(message));

    }

    @PatchMapping("/user/schedule/modify")
    public ResponseEntity<Result<ModifyScheduleDTO>> modifySchedule(@RequestBody ModifyScheduleDTO modifyScheduleDTO) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        Schedule existingSchedule = scheduleServiceImpl.getScheduleByIdAndUserId(modifyScheduleDTO.getId(), userId);

        if (modifyScheduleDTO != null) {
            existingSchedule.setStartDate(modifyScheduleDTO.getStartDate());
        }

        if (modifyScheduleDTO != null) {
            existingSchedule.setEndDate(modifyScheduleDTO.getEndDate());
        }

        Schedule modifiedSchedule = scheduleServiceImpl.modifySchedule(modifyScheduleDTO.getId(), existingSchedule.getStartDate(), existingSchedule.getEndDate());

        ModifyScheduleDTO modifyScheduleResponseDTO = ModifyScheduleDTO.from(modifiedSchedule);


        return ResponseEntity.ok(ApiResponse.success(modifyScheduleResponseDTO));
    }

    @GetMapping("/user/schedule/list")
    public ResponseEntity<Result<List<GetAllScheduleDTO>>> getAllSchedules(
        @RequestParam(name = "year", required = true) Integer year) {

        List<Schedule> allSchedules;

       LocalDate startDate = LocalDate.of(year, 1, 1);
       LocalDate endDate = LocalDate.of(year, 12, 31);
       allSchedules = scheduleServiceImpl.findAllByYear(startDate, endDate);


        List<GetAllScheduleDTO> allSchedulesDTO = allSchedules.stream()
            .map(schedule -> GetAllScheduleDTO.from(schedule))
            .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(allSchedulesDTO));

    }

}
