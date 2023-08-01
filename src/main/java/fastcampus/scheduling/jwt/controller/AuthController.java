package fastcampus.scheduling.jwt.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.CookieProvider;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.dto.RefreshAccessTokenDto;
import fastcampus.scheduling.jwt.service.RefreshTokenService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/v1/auth/refresh-token")
	public ResponseEntity<ApiResponse.Result<RefreshAccessTokenDto>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = cookieProvider.getRefreshToken(request);

		jwtTokenProvider.validateJwtToken(refreshToken);

		String userId = jwtTokenProvider.getUserId(refreshToken);

		//generate new refresh token and update to db
		String newRefreshToken = jwtTokenProvider.generateJwtRefreshToken(userId);
		refreshTokenService.updateRefreshToken(userId, refreshToken, newRefreshToken);

		//set new refresh token to cookie
		cookieProvider.addCookie(response, newRefreshToken);

		//generate new access token
		RefreshAccessTokenDto refreshAccessTokenDto = refreshTokenService.refreshAccessToken(userId);

		return ResponseEntity.ok(ApiResponse.success(refreshAccessTokenDto));
	}

}
