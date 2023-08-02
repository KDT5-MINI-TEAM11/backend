package com.fastcampus.scheduling._core.errors.exception;

import com.fastcampus.scheduling._core.errors.ErrorMessage;

public class DuplicateUserEmailException extends RuntimeException {

    public DuplicateUserEmailException() {
        super(ErrorMessage.DUPLICATE_USEREMAIL);
    }
}
