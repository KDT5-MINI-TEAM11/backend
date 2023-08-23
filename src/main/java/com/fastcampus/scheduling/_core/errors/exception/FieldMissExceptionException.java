package com.fastcampus.scheduling._core.errors.exception;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import org.springframework.security.core.AuthenticationException;

public class FieldMissExceptionException extends AuthenticationException {

    public FieldMissExceptionException() {
        super(ErrorMessage.INVALID_SIGNIN_REQUEST);
    }
}
