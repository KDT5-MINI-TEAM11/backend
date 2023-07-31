package fastcampus.scheduling.user.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.dto.UserResponse.GetMyPageDTO;
import fastcampus.scheduling.user.dto.UserResponse.GetUserHeaderDTO;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
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

        Long userId = Long.valueOf(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        User user = userService.findByUserId(userId);
        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(user);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

    @PutMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> updateMyPage(@RequestBody UserRequest.UpdateDTO updateDTO) {

        Long userId = Long.valueOf(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        User user = userService.findByUserId(userId);

        String userPassword = updateDTO.getUserPassword();
        if (userPassword != null) {
            user.setUserPassword(userPassword);
        }

        String phoneNumber = updateDTO.getPhoneNumber();
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }

        String profileThumbUrl = updateDTO.getProfileThumbUrl();
        if (profileThumbUrl != null) {
            user.setProfileThumbUrl(profileThumbUrl);
        }

        User updatedUser = userService.updateUser(userId, user.getUserPassword(), user.getPhoneNumber(), user.getProfileThumbUrl());

        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(updatedUser);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<ApiResponse.Result<UserResponse.SignUpDTO>> signUp(
        HttpServletRequest request, @RequestBody @Valid UserRequest.SignUpDTO signUpDTO, Errors errors) {
        log.info("/api/v1/auth/signup POST " + signUpDTO);
        UserResponse.SignUpDTO signUpResponse = userService.save(request, signUpDTO); // 중복등은 서비스에서 체크

        return ResponseEntity.ok(ApiResponse.success(signUpResponse));
    }

    @PostMapping("/api/v1/auth/checkPhoneNumber")
    public ResponseEntity<ApiResponse.Result<Object>> checkPhoneNumber(@RequestBody @Valid UserRequest.CheckPhoneDTO checkPhoneDTO, Errors erros) {
        log.info("/api/v1/auth/checkPhoneNumber POST " + checkPhoneDTO);
        userService.checkPhone(checkPhoneDTO);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
