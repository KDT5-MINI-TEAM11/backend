package com.fastcampus.scheduling.admin.controller;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.errors.exception.Exception403;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling.admin.dto.AdminRequest;
import com.fastcampus.scheduling.admin.dto.AdminRequest.ApproveDTO;
import com.fastcampus.scheduling.admin.dto.AdminRequest.PendingDTO;
import com.fastcampus.scheduling.admin.dto.AdminRequest.RejectDTO;
import com.fastcampus.scheduling.admin.dto.AdminResponse;
import com.fastcampus.scheduling.admin.dto.AdminResponse.GetAllUserDTO;
import com.fastcampus.scheduling.admin.service.AdminService;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        validatePosition(userId);

        List<AdminResponse.GetAllScheduleDTO> schedulingResponses = adminService.findAllSchedule();

        return ResponseEntity.ok(ApiResponse.success(schedulingResponses));
    }

    @GetMapping("/api/v1/admin/worker-list")
    public ResponseEntity<ApiResponse.Result<List<AdminResponse.GetAllUserDTO>>> getAllUser(){
        log.info("/api/v1/admin/worker-list GET");
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        validatePosition(userId);
        List<GetAllUserDTO> userResponse = adminService.findAllUser();

        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @PostMapping("/api/v1/admin/reject")
    public ResponseEntity<ApiResponse.Result<Object>> reject(HttpServletRequest request, @RequestBody RejectDTO rejectDTO){
        log.info("/api/v1/admin/reject POST" + rejectDTO);
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        validatePosition(userId);
        String result = adminService.updateScheduleReject(rejectDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/api/v1/admin/approve")
    public ResponseEntity<ApiResponse.Result<Object>> approve(HttpServletRequest request, @RequestBody ApproveDTO approveDTO){
        log.info("/api/v1/admin/approve POST" + approveDTO);
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        validatePosition(userId);
        String result = adminService.updateScheduleApprove(approveDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/api/v1/admin/change-position")
    public ResponseEntity<ApiResponse.Result<Object>> position(HttpServletRequest request, @RequestBody AdminRequest.UpdatePositionDTO updatePositionDTO){
        log.info("/api/v1/admin/change-position POST" + updatePositionDTO);
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        validatePosition(userId);
        if(userId.equals(updatePositionDTO.getId()))
            throw new Exception400(ErrorMessage.INVALID_CHANGE_POSITION);

        String result = adminService.updatePosition(updatePositionDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/api/v1/admin/pending")
    public ResponseEntity<ApiResponse.Result<Object>> cancel(HttpServletRequest request, @RequestBody PendingDTO pendingDTO){
        log.info("/api/v1/admin/pending POST" + pendingDTO);
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        validatePosition(userId);
        String result = adminService.updateSchedulePending(pendingDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private void validatePosition(Long userId) { //aop로
        User user = userService.findByUserId(userId);
        if(!user.getPosition().equals(Position.MANAGER))
            throw new Exception403(ErrorMessage.INVALID_POSITION);
    }
}
