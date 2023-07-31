package fastcampus.scheduling.user.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.CookieProvider;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.service.RefreshTokenService;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.dto.UserResponse.GetMyPageDTO;
import fastcampus.scheduling.user.dto.UserResponse.GetUserHeaderDTO;
import fastcampus.scheduling.user.dto.UserResponse.SignUpDTO;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CookieProvider cookieProvider;

    @GetMapping("/api/v1/user/userHeader")
    public ResponseEntity<ApiResponse.Result<GetUserHeaderDTO>> getUserHead() {
        log.info("/api/v1/user/userHeader GET ");

        Long userId = Long.valueOf(
						SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        User user = userService.findByUserId(userId);

        GetUserHeaderDTO getUserHeaderDTO = GetUserHeaderDTO.from(user);

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
        HttpServletRequest request, @RequestBody @Valid UserRequest.SignUpDTO signUpDTO, HttpServletResponse response) {
        log.info("/api/v1/auth/signup POST " + signUpDTO);
        User user = userService.save(signUpDTO); // 중복등은 서비스에서 체크

        String userEmail = user.getUserEmail();
        org.springframework.security.core.userdetails.User savedUser = (org.springframework.security.core.userdetails.User) userService.loadUserByUsername(userEmail);
        String userId = savedUser.getUsername();
        List<String> roles = savedUser.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        String accessToken = jwtTokenProvider.generateJwtAccessToken(userId, request.getRequestURI(), roles);
        String refreshToken = jwtTokenProvider.generateJwtRefreshToken(userId);
        refreshTokenService.saveRefreshToken(Long.valueOf(userId), jwtTokenProvider.getRefreshTokenId(refreshToken));
        response = cookieProvider.addCookie(response, refreshToken);


        return ResponseEntity.ok(ApiResponse.success(SignUpDTO.from(accessToken)));
    }

    @PostMapping("/api/v1/auth/checkPhoneNumber")
    public ResponseEntity<ApiResponse.Result<Object>> checkPhoneNumber(@RequestBody @Valid UserRequest.CheckPhoneDTO checkPhoneDTO) {
        log.info("/api/v1/auth/checkPhoneNumber POST " + checkPhoneDTO);
        userService.checkPhone(checkPhoneDTO);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
