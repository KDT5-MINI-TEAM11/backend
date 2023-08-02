package com.fastcampus.scheduling.admin.controller;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling.admin.service.AdminService;
import com.fastcampus.scheduling.schedule.dto.SchedulingResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/api/v1/admin/list")
    public ResponseEntity<ApiResponse.Result<List<SchedulingResponse>>> getSchedule(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        //authorization.startsWith("Bearer ")

        List<SchedulingResponse> schedulingResponses = adminService.find();

        return ResponseEntity.ok(ApiResponse.success(schedulingResponses));
    }
}
