package com.fastcampus.scheduling.email.controller;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling.email.dto.EmailRequest;
import com.fastcampus.scheduling.email.service.MailService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MailApiController {

    private final MailService mailService;
    @PostMapping("/api/v1/auth/check-email")
    public ResponseEntity<ApiResponse.Result<Object>> checkEmail(@RequestBody @Valid EmailRequest.CheckEmailDTO checkEmailDTO, Errors errors) {
        log.info("/api/v1/auth/checkEmail POST " + checkEmailDTO);
        boolean isChecked = mailService.checkEmail(checkEmailDTO);

        return ResponseEntity.ok(ApiResponse.success(isChecked));
    }

    @PostMapping("/api/v1/auth/send-email")
    public ResponseEntity<ApiResponse.Result<Object>> sendEmail(@RequestBody @Valid EmailRequest.SendEmailDTO sendEmailDTO, Errors errors) {
        log.info("/api/v1/auth/sendEmail POST " + sendEmailDTO);
        boolean isSent = mailService.sendEmail(sendEmailDTO);

        return ResponseEntity.ok(ApiResponse.success(isSent));
    }

//    @PostMapping("/api/v2/auth/sendEmail")
//    public ResponseEntity<ApiResponse.Result<Object>> sendEmail(@RequestBody @Valid EmailRequest.SendEmailDTO sendEmailDTO, Errors errors) {
//        log.info("/api/v1/auth/sendEmail POST " + sendEmailDTO);
//        boolean authEmail = mailService.sendEmail(sendEmailDTO);
//
//        return ResponseEntity.ok(ApiResponse.success(authEmail));
//    }

    @PostMapping("/api/v1/auth/check-email-auth")
    public ResponseEntity<ApiResponse.Result<Object>> checkEmailAuth(@RequestBody @Valid EmailRequest.CheckEmailAuthDTO checkEmailAuthDTO, Errors errors) {
        log.info("/api/v1/auth/checkEmailAuth POST " + checkEmailAuthDTO);
        String result = mailService.checkEmailAuth(checkEmailAuthDTO);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

}
