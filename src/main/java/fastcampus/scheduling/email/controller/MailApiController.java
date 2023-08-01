package fastcampus.scheduling.email.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling.email.dto.EmailRequest;
import fastcampus.scheduling.email.service.MailService;
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
    @PostMapping("/api/v1/auth/checkEmail")
    public ResponseEntity<ApiResponse.Result<Object>> checkEmail(@RequestBody @Valid EmailRequest.CheckEmailDTO checkEmailDTO, Errors errors) {
        log.info("/api/v1/auth/checkEmail POST " + checkEmailDTO);
        mailService.checkEmail(checkEmailDTO);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/api/v1/auth/sendEmail")
    public ResponseEntity<ApiResponse.Result<Object>> sendEmail(@RequestBody @Valid EmailRequest.SendEmailDTO sendEmailDTO, Errors errors) {
        log.info("/api/v1/auth/sendEmail POST " + sendEmailDTO);
        boolean authEmail = mailService.sendEmail(sendEmailDTO);

        return ResponseEntity.ok(ApiResponse.success(authEmail));
    }

    @PostMapping("/api/v1/auth/checkEmailAuth")
    public ResponseEntity<ApiResponse.Result<Object>> checkEmailAuth(@RequestBody @Valid EmailRequest.CheckEmailAuthDTO checkEmailAuthDTO, Errors errors) {
        log.info("/api/v1/auth/checkEmailAuth POST " + checkEmailAuthDTO);
        boolean checkEmailAuth = mailService.checkEmailAuth(checkEmailAuthDTO);

        return ResponseEntity.ok(ApiResponse.success(checkEmailAuth));
    }

}
