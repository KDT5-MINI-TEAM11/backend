package fastcampus.scheduling.user.exception;

import lombok.Getter;

@Getter
public enum UserExceptionMessage {
	USER_NOT_FOUND_EXCEPTION("User Not Found");

	private final String message;
	UserExceptionMessage(String message) {
		this.message = message;
	}
}
