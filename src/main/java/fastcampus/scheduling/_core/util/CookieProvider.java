package fastcampus.scheduling._core.util;

import fastcampus.scheduling.jwt.exception.JwtExceptionMessage;
import io.jsonwebtoken.JwtException;
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
				.secure(true)
				.path("/")
				.maxAge(Long.parseLong(refreshTokenExpiredTime))
				.build();
	}

	public ResponseCookie removeRefreshTokenCookie() {
		return ResponseCookie.from("refresh-token", null)
				.maxAge(0)
				.path("/")
				.build();
	}

	public String getRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("refresh_token")) {
					return cookie.getValue();
				}
			}
		}
		throw new JwtException(JwtExceptionMessage.TOKEN_NOT_VALID.getMessage());
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
