package fastcampus.scheduling._core.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.exception.CustomException;
import fastcampus.scheduling._core.util.ApiResponse;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
			FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (CustomException exception) {
			HttpStatus status = exception.getStatus();
			String message = exception.getMessage();

			log.warn("CustomException occurred {}, {}", status, message);
			response.setContentType("application/json");
			response.setStatus(status.value());
			new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.error(message, status));
		}

	}
}
