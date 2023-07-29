package fastcampus.scheduling._core.util;

import static fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;

import fastcampus.scheduling._core.errors.exception.Exception401;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

	@Value("${jwt.refresh-token.expiration}")
	private String refreshTokenExpiredTime;

	public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
		return ResponseCookie.from("refresh-token", refreshToken)
				.httpOnly(true)
				//.secure(true)
				.secure(false)
				.path("/")
				.maxAge(Long.parseLong(refreshTokenExpiredTime))
				.build();
	}

	public String getRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("refresh-token")) {
					return cookie.getValue();
				}
			}
		}
		throw new Exception401(TOKEN_NOT_VALID);
	}

	public Cookie of(ResponseCookie responseCookie) {
		Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
		cookie.setPath(responseCookie.getPath());
		cookie.setSecure(responseCookie.isSecure());
		cookie.setHttpOnly(responseCookie.isHttpOnly());
		cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
		return cookie;
	}
}
