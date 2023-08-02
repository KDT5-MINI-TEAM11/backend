package com.fastcampus.scheduling.admin.controller;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.JwtTokenProvider;
import com.fastcampus.scheduling.admin.dto.AdminRequest;
import com.fastcampus.scheduling.admin.dto.AdminRequest.ApproveDTO;
import com.fastcampus.scheduling.admin.dto.AdminRequest.RejectDTO;
import com.fastcampus.scheduling.admin.dto.AdminResponse;
import com.fastcampus.scheduling.admin.service.AdminService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/v1/admin/list")
    public ResponseEntity<ApiResponse.Result<List<AdminResponse.GetAllScheduleDTO>>> getAllSchedule(){

        List<AdminResponse.GetAllScheduleDTO> schedulingResponses = adminService.findAll();

        return ResponseEntity.ok(ApiResponse.success(schedulingResponses));
    }

    @PostMapping("/api/v1/admin/reject")
    public ResponseEntity<ApiResponse.Result<AdminResponse.ResolveDTO>> reject(HttpServletRequest request, @RequestBody RejectDTO rejectDTO){
        rejectDTO.setUserId(getUserId(request));
        AdminResponse.ResolveDTO responseResolveDTO = adminService.updateScheduleReject(rejectDTO);

        return ResponseEntity.ok(ApiResponse.success(responseResolveDTO));
    }
    @PostMapping("/api/v1/admin/approve")
    public ResponseEntity<ApiResponse.Result<AdminResponse.ResolveDTO>> resolve(HttpServletRequest request, @RequestBody ApproveDTO resolveDTO){
        resolveDTO.setUserId(getUserId(request));
        AdminResponse.ResolveDTO responseResolveDTO = adminService.updateScheduleApprove(resolveDTO);

        return ResponseEntity.ok(ApiResponse.success(responseResolveDTO));
    }
    @PostMapping("/api/v1/admin/position")
    public ResponseEntity<ApiResponse.Result<Object>> position(HttpServletRequest request, @RequestBody AdminRequest.UpdatePositionDTO updatePositionDTO){
        updatePositionDTO.setId(getUserId(request));
        adminService.updatePosition(updatePositionDTO);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    public Long getUserId(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            throw new Exception401(ErrorMessage.TOKEN_NOT_EXISTS);

        String accessToken = authorizationHeader.substring(7);
        return Long.valueOf(jwtTokenProvider.getUserId(accessToken));
    }
}
