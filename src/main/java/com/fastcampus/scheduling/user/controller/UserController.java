package com.fastcampus.scheduling.user.controller;

import com.fastcampus.scheduling._core.security.annotation.CurrentUser;
import com.fastcampus.scheduling._core.security.dto.SignInResponse;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import com.fastcampus.scheduling._core.util.CookieProvider;
import com.fastcampus.scheduling._core.util.JwtTokenProvider;
import com.fastcampus.scheduling.jwt.service.RefreshTokenService;
import com.fastcampus.scheduling.user.dto.UserRequest;
import com.fastcampus.scheduling.user.dto.UserResponse;
import com.fastcampus.scheduling.user.dto.UserResponse.GetMyPageDTO;
import com.fastcampus.scheduling.user.dto.UserResponse.GetUserHeaderDTO;
import com.fastcampus.scheduling.user.dto.UserResponse.SignUpDTO;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.service.UserLogService;
import com.fastcampus.scheduling.user.service.UserService;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CookieProvider cookieProvider;
    private final UserLogService userLogService;

    @GetMapping("/api/v1/user/userHeader")
    public ResponseEntity<Result<GetUserHeaderDTO>> getUserHead(@CurrentUser User user) {
        log.info("/api/v1/user/userHeader GET ");

        GetUserHeaderDTO getUserHeaderDTO = GetUserHeaderDTO.from(user);

        return ResponseEntity.ok(ApiResponse.success(getUserHeaderDTO));
    }
    @GetMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> getMyPage(@CurrentUser User user) {

        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(user);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

    @PatchMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> updateMyPage(@RequestBody UserRequest.UpdateDTO updateDTO, @CurrentUser User user) {

        User updatedUser = userService.updateUser(user, updateDTO);

        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(updatedUser);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<ApiResponse.Result<UserResponse.SignUpDTO>> signUp(
        HttpServletRequest request, @RequestBody @Valid UserRequest.SignUpDTO signUpDTO, HttpServletResponse response) {
        log.info("/api/v1/auth/signup POST " + signUpDTO);

        User user = userService.save(signUpDTO);

        String userEmail = user.getUserEmail();
        Long userId = user.getId();
        Collection<GrantedAuthority> authorities = userService.getAuthorities(user);

        String accessToken = jwtTokenProvider.generateJwtAccessToken(userEmail, request.getRequestURI(), authorities);
        String refreshToken = jwtTokenProvider.generateJwtRefreshToken(userId.toString());
        refreshTokenService.saveRefreshToken(userId, jwtTokenProvider.getRefreshTokenId(refreshToken));
        cookieProvider.addCookie(response, refreshToken);

        userLogService.saveSignInLog(user);

        return ResponseEntity.ok(ApiResponse.success(SignUpDTO.from(accessToken)));
    }

    @PostMapping("/api/v2/auth/signup")
    public ResponseEntity<ApiResponse.Result<SignInResponse>> signUpV2(
        HttpServletRequest request, @RequestBody @Valid UserRequest.SignUpDTO signUpDTO, HttpServletResponse response) {
        log.info("/api/v2/auth/signup POST " + signUpDTO);

        User user = userService.save(signUpDTO); // 중복등은 서비스에서 체크

        String userEmail = user.getUserEmail();
        Long userId = user.getId();
        Collection<GrantedAuthority> authorities = userService.getAuthorities(user);

        String accessToken = jwtTokenProvider.generateJwtAccessToken(userEmail, request.getRequestURI(), authorities);
        String refreshToken = jwtTokenProvider.generateJwtRefreshToken(userId.toString());
        refreshTokenService.saveRefreshToken(userId, jwtTokenProvider.getRefreshTokenId(refreshToken));
        cookieProvider.addCookie(response, refreshToken);

        userLogService.saveSignInLog(user);

        SignInResponse signinResponse = SignInResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
        return ResponseEntity.ok(ApiResponse.success(signinResponse));
    }
}
