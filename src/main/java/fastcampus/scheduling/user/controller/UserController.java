package fastcampus.scheduling.user.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.ApiResponse.Result;
import fastcampus.scheduling.email.dto.EmailRequest;
import fastcampus.scheduling.email.dto.EmailResponse;
import fastcampus.scheduling.email.dto.EmailResponse.AuthEmailDTO;
import fastcampus.scheduling.email.service.MailService;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.dto.UserResponse.GetMyPageDTO;
import fastcampus.scheduling.user.dto.UserResponse.GetUserHeaderDTO;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @GetMapping("/api/v1/user/userHeader")
    public ResponseEntity<ApiResponse.Result<GetUserHeaderDTO>> getUserHead() {
        log.info("/api/v1/user/userHeader GET ");

        Long userId = Long.valueOf(
						SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        User user = userService.findByUserId(userId);

        int totalVacation = user.getPosition().getTotalVacation();
        int usedVacation = user.getUsedVacation();

        GetUserHeaderDTO getUserHeaderDTO = GetUserHeaderDTO.from(user, usedVacation, totalVacation);

        return ResponseEntity.ok(ApiResponse.success(getUserHeaderDTO));
    }
    @GetMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> getMyPage() {
        Long userId = 1L;

        User user = userService.findByUserId(userId);
        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(user);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

    @PutMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> updateMyPage(@RequestBody UserRequest.UpdateDTO updateDTO) {
        Long userId = 1L;

        String userPassword = updateDTO.getUserPassword();
        String phoneNumber = updateDTO.getPhoneNumber();
        String profileThumbUrl = updateDTO.getProfileThumbUrl();

        User user = userService.findByUserId(userId);

        user.setUserPassword(userPassword);
        user.setPhoneNumber(phoneNumber);
        user.setProfileThumbUrl(profileThumbUrl);

        User updatedUser = userService.updateUser(userId, userPassword, phoneNumber, profileThumbUrl);

        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(updatedUser);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

    @PostMapping("/api/v1/auth/checkEmail")
    public ResponseEntity<Result<Object>> checkEmail(@RequestBody @Valid EmailRequest.CheckEmailDTO checkEmailDTO, Errors errors) {
        log.info("/api/v1/auth/checkEmail POST " + checkEmailDTO);
        mailService.checkEmail(checkEmailDTO);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/api/v1/auth/sendEmail")
    public ResponseEntity<ApiResponse.Result<EmailResponse.AuthEmailDTO>> sendEmail(@RequestBody @Valid EmailRequest.SendEmailDTO sendEmailDTO, Errors errors) {
        log.info("/api/v1/auth/sendEmail POST " + sendEmailDTO);
        AuthEmailDTO authEmail = mailService.sendEmail(sendEmailDTO);

        return ResponseEntity.ok(ApiResponse.success(authEmail));
    }
}
