package fastcampus.scheduling.user.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
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
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<ApiResponse.Result<UserResponse.SignUpDTO>> signUp(HttpServletRequest request, @RequestBody @Valid UserRequest.SignUpDTO signUpDTO, Errors errors) {
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
