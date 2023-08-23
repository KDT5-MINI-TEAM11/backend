package com.fastcampus.scheduling.jwt.exception;

import com.fastcampus.scheduling._core.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {

	public UnauthorizedException(HttpStatus status, String message) {
		super(status, message);
	}
}
