package fastcampus.scheduling.user.exception;

import lombok.Getter;

@Getter
public enum UserExceptionMessage {
	USER_NOT_FOUND_EXCEPTION("유저가 존재 하지 않습니다.");

	private final String message;
	UserExceptionMessage(String message) {
		this.message = message;
	}
}
