package com.fastcampus.scheduling._core.util;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fastcampus.scheduling._core.errors.exception.Exception401;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

	public HttpServletResponse addCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie responseCookie = generateRefreshTokenCookie(refreshToken);
		Cookie cookie = of(responseCookie);
		response.addCookie(cookie);
		response.setStatus(SC_OK);
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("utf-8");
		return response;
	}
}
