package com.fastcampus.scheduling.jwt.controller;

import com.fastcampus.scheduling._core.security.dto.SignInResponse;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.CookieProvider;
import com.fastcampus.scheduling._core.util.JwtTokenProvider;
import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenRequestDto;
import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenResponseDto;
import com.fastcampus.scheduling.jwt.service.RefreshTokenService;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

	private final RefreshTokenService refreshTokenService;
	private final UserService userService;
	private final CookieProvider cookieProvider;
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/v1/auth/refresh-token")
	public ResponseEntity<ApiResponse.Result<RefreshAccessTokenResponseDto>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = cookieProvider.getRefreshToken(request);

		jwtTokenProvider.validateJwtToken(refreshToken);

		String userId = jwtTokenProvider.getSubject(refreshToken);

		//generate new refresh token and update to db
		String newRefreshToken = jwtTokenProvider.generateJwtRefreshToken(userId);
		refreshTokenService.updateRefreshToken(userId, refreshToken, newRefreshToken);

		//set new refresh token to cookie
		cookieProvider.addCookie(response, newRefreshToken);

		User user = userService.findByUserId(Long.valueOf(userId));

		//generate new access token
		String newAccessToken = jwtTokenProvider.generateJwtAccessToken(user.getUserEmail(), request.getRequestURI(), userService.getAuthorities(user));

		RefreshAccessTokenResponseDto refreshAccessTokenResponseDto = refreshTokenService.refreshAccessToken(newAccessToken);

		return ResponseEntity.ok(ApiResponse.success(refreshAccessTokenResponseDto));
	}

	@PostMapping("/v2/auth/refresh-token")
	public ResponseEntity<ApiResponse.Result<SignInResponse>> refreshTokenTemp(@RequestBody
		RefreshAccessTokenRequestDto refreshAccessTokenRequestDto, HttpServletRequest request, HttpServletResponse response) {

		String refreshToken = refreshTokenService.getRefreshToken(refreshAccessTokenRequestDto);

		String userId = jwtTokenProvider.getSubject(refreshToken);

		//generate new refresh token and update to db
		String newRefreshToken = jwtTokenProvider.generateJwtRefreshToken(userId);
		refreshTokenService.updateRefreshToken(userId, refreshToken, newRefreshToken);

		//set new refresh token to cookie
		cookieProvider.addCookie(response, newRefreshToken);

		//generate new access token
		User user = userService.findByUserId(Long.valueOf(userId));

		//generate new access token
		String newAccessToken = jwtTokenProvider.generateJwtAccessToken(user.getUserEmail(), request.getRequestURI(), userService.getAuthorities(user));

		SignInResponse signinResponse = SignInResponse.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken)
				.build();
		return ResponseEntity.ok(ApiResponse.success(signinResponse));
	}

}
