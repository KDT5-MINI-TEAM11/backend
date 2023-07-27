package fastcampus.scheduling.jwt.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.CookieProvider;
import fastcampus.scheduling.jwt.dto.JwtTokenDto;
import fastcampus.scheduling.jwt.service.RefreshTokenService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

	private final RefreshTokenService refreshTokenService;
	private final CookieProvider cookieProvider;

	@GetMapping("/v1/auth/refresh-token")
	public ResponseEntity<ApiResponse.Result<String>> refreshToken(HttpServletRequest request) {
		String refreshToken = cookieProvider.getRefreshToken(request);
		JwtTokenDto jwtTokenDto = refreshTokenService.refreshAccessToken(refreshToken);

		ResponseCookie responseCookie = cookieProvider.generateRefreshTokenCookie(refreshToken);
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(ApiResponse.success(jwtTokenDto.getAccessToken()));
	}

	@PostMapping("/v1/auth/signout")
	public ResponseEntity<ApiResponse.Result<String>> logout(@RequestHeader(name = "Authorization") HttpServletRequest request) {
		String refreshToken = cookieProvider.getRefreshToken(request);
		String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		refreshTokenService.revokeToken(userId, refreshToken);

		ResponseCookie refreshCookie = cookieProvider.removeRefreshTokenCookie();

		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
				.body(ApiResponse.success("로그아웃 완료"));
	}

}
