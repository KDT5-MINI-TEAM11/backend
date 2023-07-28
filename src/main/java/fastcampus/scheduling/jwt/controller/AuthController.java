package fastcampus.scheduling.jwt.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.CookieProvider;
import fastcampus.scheduling.jwt.dto.RefreshAccessTokenDto;
import fastcampus.scheduling.jwt.service.RefreshTokenService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

	private final RefreshTokenService refreshTokenService;
	private final CookieProvider cookieProvider;

	@GetMapping("/v1/auth/refresh-token")
	public ResponseEntity<ApiResponse.Result<RefreshAccessTokenDto>> refreshToken(HttpServletRequest request) {
		String refreshToken = cookieProvider.getRefreshToken(request);
		RefreshAccessTokenDto refreshAccessTokenDto = refreshTokenService.refreshAccessToken(refreshToken);

		ResponseCookie responseCookie = cookieProvider.generateRefreshTokenCookie(refreshToken);
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(ApiResponse.success(refreshAccessTokenDto));
	}

}
