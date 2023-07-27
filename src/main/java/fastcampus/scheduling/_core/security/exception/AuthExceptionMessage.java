package fastcampus.scheduling._core.security.exception;

import lombok.Getter;

@Getter
public enum AuthExceptionMessage {
	MISMATCH_SIGN_IN_INFO("인증에 실패했습니다. 사용자 ID 또는 패스워드가 잘못되었습니다."),
	INVALID_REQUEST("로그인 요청이 잘못되었습니다. 필수 필드가 누락되었습니다.");

	private final String message;

	AuthExceptionMessage(String message) {
		this.message = message;
	}
}
