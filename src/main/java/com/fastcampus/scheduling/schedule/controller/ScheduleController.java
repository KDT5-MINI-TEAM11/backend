package com.fastcampus.scheduling.schedule.controller;

import com.fastcampus.scheduling._core.exception.CustomException;
import com.fastcampus.scheduling._core.security.annotation.CurrentUser;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import com.fastcampus.scheduling.schedule.dto.ScheduleRequest;
import com.fastcampus.scheduling.schedule.dto.ScheduleRequest.ModifyScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.AddScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.GetAllScheduleDTO;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.GetUserScheduleDTO;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.service.ScheduleServiceImpl;
import com.fastcampus.scheduling.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Result<List<GetUserScheduleDTO>>> getUserSchedule(@RequestParam(name = "year", required = true) Integer year, @CurrentUser User user) {

        List<Schedule> schedules;

        LocalDateTime startDate = calculateStartDateTime(year);
        LocalDateTime endDate = calculateEndDateTime(year);
        schedules = scheduleServiceImpl.findByYear(user.getId(), startDate, endDate);

        List<GetUserScheduleDTO> userSchedulesDTO = schedules.stream()
            .map(schedule -> GetUserScheduleDTO.from(schedule))
            .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(userSchedulesDTO));
    }

    @PostMapping("/user/schedule/add")
    public ResponseEntity<Result<ScheduleResponse.AddScheduleDTO>> addSchedule(@RequestBody ScheduleRequest.AddScheduleDTO addScheduleDTO, @CurrentUser User user) {

        addScheduleDTO.setUserId(user.getId());

        Schedule savedSchedule = scheduleServiceImpl.addSchedule(addScheduleDTO);
        ScheduleResponse.AddScheduleDTO addSchedule = AddScheduleDTO.from(savedSchedule);

        return ResponseEntity.ok(ApiResponse.success(addSchedule));
    }

    @PostMapping("/user/schedule/cancel")
    public ResponseEntity<Result> cancelSchedule(@RequestBody Map<String, String> request, @CurrentUser User user) throws CustomException {

        Long id = Long.valueOf(request.get("id"));
        scheduleServiceImpl.cancelSchedule(id, user.getId());

        String message = "정상적으로 취소 되었습니다";
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @GetMapping("/user/schedule/pending-list")
    public ResponseEntity<Result> getPendingSchedule(@RequestParam(name = "year", required = true) Integer year, @CurrentUser User user) {

        List<Schedule> schedules;

        LocalDateTime startDate = calculateStartDateTime(year);
        LocalDateTime endDate = calculateEndDateTime(year);

        schedules = scheduleServiceImpl.findMyPendingSchedule(user.getId(), startDate, endDate);

        List<GetUserScheduleDTO> userSchedulesDTO = schedules.stream()
            .map(schedule -> GetUserScheduleDTO.from(schedule))
            .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(userSchedulesDTO));
    }

    @PatchMapping("/user/schedule/modify")
    public ResponseEntity<Result<ModifyScheduleDTO>> modifySchedule(@RequestBody ModifyScheduleDTO modifyScheduleDTO, @CurrentUser User user) {

        Schedule modifiedSchedule = scheduleServiceImpl.modifySchedule(modifyScheduleDTO, user.getId());

        ModifyScheduleDTO modifyScheduleResponseDTO = ModifyScheduleDTO.from(modifiedSchedule);

        return ResponseEntity.ok(ApiResponse.success(modifyScheduleResponseDTO));
    }

    @GetMapping("/schedule/list")
    public ResponseEntity<Result<List<GetAllScheduleDTO>>> getAllSchedules(@RequestParam(name = "year", required = true) Integer year) {
        List<Schedule> allSchedules;

        LocalDateTime startDate = calculateStartDateTime(year);
        LocalDateTime endDate = calculateEndDateTime(year);
        allSchedules = scheduleServiceImpl.findAllByYear(startDate, endDate);

        List<GetAllScheduleDTO> allSchedulesDTO = allSchedules.stream()
            .map(schedule -> GetAllScheduleDTO.from(schedule))
            .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(allSchedulesDTO));
    }

    private LocalDateTime calculateStartDateTime(Integer year){
        return LocalDateTime.of((year - 1), 12, 26, 0, 0);
    }

    private LocalDateTime calculateEndDateTime(Integer year){
        return LocalDateTime.of((year + 1), 1, 11, 23, 59);
    }

}
