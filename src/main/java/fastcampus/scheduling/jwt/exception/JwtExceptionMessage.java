package fastcampus.scheduling.jwt.exception;

import lombok.Getter;

@Getter
public enum JwtExceptionMessage {
	TOKEN_NOT_EXISTS("토큰을 확인 할 수 없습니다."),
	TOKEN_NOT_VALID("비정상적인 토큰입니다."),
	TOKEN_EXPIRED("토큰이 만료 되었습니다.");

	private final String message;
	JwtExceptionMessage(String message) {
		this.message = message;
	}
}
