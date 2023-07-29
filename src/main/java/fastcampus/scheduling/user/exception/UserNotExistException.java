package fastcampus.scheduling.user.exception;

import fastcampus.scheduling._core.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotExistException extends CustomException {

	public UserNotExistException(String message) {
		super(HttpStatus.UNAUTHORIZED, message );
	}
}
