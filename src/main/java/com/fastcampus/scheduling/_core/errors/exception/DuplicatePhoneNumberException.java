package com.fastcampus.scheduling._core.errors.exception;

import com.fastcampus.scheduling._core.errors.ErrorMessage;

public class DuplicatePhoneNumberException extends RuntimeException {
    public DuplicatePhoneNumberException() {
        super(ErrorMessage.DUPLICATE_PHONENUMBER);
    }
}
