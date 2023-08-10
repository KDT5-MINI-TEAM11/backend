package com.fastcampus.scheduling._core.security.handler;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.INNER_SERVER_ERROR;
import static com.fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;

import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling._core.exception.CustomException;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomExceptionHandler extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			String message = INNER_SERVER_ERROR;
			ApiResponse.Result<Object> responseBody = ApiResponse.error(message, status);
			if (exception instanceof CustomException) {
				status = ((CustomException) exception).getStatus();
				message = exception.getMessage();
				responseBody = ApiResponse.error(message, status);
			}
			if (exception instanceof Exception401) {
				responseBody =  ((Exception401) exception).body();
			}

			if (exception instanceof Exception400) {
				status = HttpStatus.BAD_REQUEST;
				responseBody =  ((Exception400) exception).body();
			}

			if (exception instanceof JwtException) {
				status = HttpStatus.UNAUTHORIZED;
				message = TOKEN_NOT_VALID;
				responseBody = ApiResponse.error(message, status);
			}

			log.warn("Exception occurred {}, {}", status, message);
			response.setContentType("application/json");
			response.setStatus(status.value());
			new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
		}

	}
}
