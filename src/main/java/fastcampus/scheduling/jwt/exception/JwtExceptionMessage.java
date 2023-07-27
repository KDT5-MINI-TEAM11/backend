package fastcampus.scheduling.jwt.exception;

import lombok.Getter;

@Getter
public enum JwtExceptionMessage {
	TOKEN_NOT_VALID("Token is Not Valid"),
	TOKEN_EXPIRED("Token is Expired");

	private final String message;
	JwtExceptionMessage(String message) {
		this.message = message;
	}
}
