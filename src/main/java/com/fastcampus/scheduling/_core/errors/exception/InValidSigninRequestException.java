package com.fastcampus.scheduling._core.errors.exception;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import org.springframework.security.core.AuthenticationException;

public class InValidSigninRequestException extends AuthenticationException {

    public InValidSigninRequestException() {
        super(ErrorMessage.MISMATCH_SIGN_IN_INFO);
    }
}
