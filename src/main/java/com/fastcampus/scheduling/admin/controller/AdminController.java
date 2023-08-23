package com.fastcampus.scheduling.admin.controller;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.security.annotation.CurrentUser;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling.admin.dto.AdminRequest;
import com.fastcampus.scheduling.admin.dto.AdminResponse;
import com.fastcampus.scheduling.admin.dto.AdminResponse.GetAllUserDTO;
import com.fastcampus.scheduling.admin.dto.AdminResponse.ResetPasswordDTO;
import com.fastcampus.scheduling.admin.service.AdminService;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/api/v1/admin/list")
    public ResponseEntity<ApiResponse.Result<List<AdminResponse.GetAllScheduleDTO>>> getAllSchedule(){
        log.info("/api/v1/admin/list GET");

        List<AdminResponse.GetAllScheduleDTO> schedulingResponses = adminService.findAllSchedule();

        return ResponseEntity.ok(ApiResponse.success(schedulingResponses));
    }

    @GetMapping("/api/v1/admin/worker-list")
    public ResponseEntity<ApiResponse.Result<List<AdminResponse.GetAllUserDTO>>> getAllUser(){
        log.info("/api/v1/admin/worker-list GET");

        List<GetAllUserDTO> userResponse = adminService.findAllUser();

        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @PostMapping("/api/v1/admin/reject")
    public ResponseEntity<ApiResponse.Result<Object>> rejectSchedule(@RequestBody AdminRequest.ScheduleDTO scheduleDTO){
        log.info("/api/v1/admin/reject POST" + scheduleDTO);

        String result = adminService.updateScheduleReject(scheduleDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/api/v1/admin/approve")
    public ResponseEntity<ApiResponse.Result<Object>> approveSchedule(@RequestBody AdminRequest.ScheduleDTO scheduleDTO){
        log.info("/api/v1/admin/approve POST" + scheduleDTO);

        String result = adminService.updateScheduleApprove(scheduleDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/api/v1/admin/change-position")
    public ResponseEntity<ApiResponse.Result<Object>> changePosition(@RequestBody AdminRequest.UpdatePositionDTO updatePositionDTO, @CurrentUser User user){
        log.info("/api/v1/admin/change-position POST" + updatePositionDTO);

        if(user.getId().equals(updatePositionDTO.getId()))
            throw new Exception400(ErrorMessage.INVALID_CHANGE_POSITION);

        String result = adminService.updatePosition(updatePositionDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/api/v1/admin/pending")
    public ResponseEntity<ApiResponse.Result<Object>> cancelSchedule(@RequestBody AdminRequest.ScheduleDTO scheduleDTO){
        log.info("/api/v1/admin/pending POST" + scheduleDTO);

        String result = adminService.updateSchedulePending(scheduleDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/api/v1/admin/reset-password")
    public ResponseEntity<ApiResponse.Result<Object>> cancelSchedule(@RequestBody AdminRequest.ResetPasswordDTO resetPasswordDTO){
        log.info("/api/v1/admin/reset-password POST");

        userService.resetUserPassword(resetPasswordDTO);

        return ResponseEntity.ok(ApiResponse.success(ResetPasswordDTO.builder().message("비밀번호가 초기화 되었습니다.").build()));
    }

}
