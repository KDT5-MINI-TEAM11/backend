package com.fastcampus.scheduling._core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
	public class CustomException extends RuntimeException{
	private HttpStatus status;
	private String message;

	protected CustomException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
