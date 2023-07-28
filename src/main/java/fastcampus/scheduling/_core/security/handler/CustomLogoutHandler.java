package fastcampus.scheduling._core.security.handler;

import fastcampus.scheduling._core.util.CookieProvider;
import fastcampus.scheduling.jwt.service.RefreshTokenService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

@Configuration
@RequiredArgsConstructor
public class CustomLogoutHandler implements
		org.springframework.security.web.authentication.logout.LogoutHandler {

	private final CookieProvider cookieProvider;
	private final RefreshTokenService refreshTokenService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {

		try {
			String refreshToken = cookieProvider.getRefreshToken(request);
			refreshTokenService.revokeToken(refreshToken);
		} catch (Exception exception) {
			// keep processing if revokeToken failed(Token validation fail -> passed token is not stored in db)
			// logout process always success
		}


	}
}
