package com.fastcampus.scheduling._core.security.exception;

import org.springframework.security.core.AuthenticationException;

public class FieldMissingException extends AuthenticationException {

	public FieldMissingException(String msg) {
		super(msg);
	}
}
