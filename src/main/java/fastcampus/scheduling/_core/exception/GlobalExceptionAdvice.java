package fastcampus.scheduling._core.exception;

import fastcampus.scheduling._core.util.ApiResponse;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ApiResponse.Error> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException exception) {
		log.error("HandleHttpRequestMethodNotSupportedException", exception);
		ApiResponse.Error errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST).getError();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(JwtException.class)
	protected ResponseEntity<ApiResponse.Error> jwtException(
			JwtException exception) {
		log.error("jwtException", exception);
		ApiResponse.Error errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.UNAUTHORIZED).getError();

		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ApiResponse.Error> exception(
			Exception exception) {
		log.error("Exception", exception);
		ApiResponse.Error errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).getError();

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
