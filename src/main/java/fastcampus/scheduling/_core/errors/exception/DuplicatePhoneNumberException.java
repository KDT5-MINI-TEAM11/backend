package fastcampus.scheduling._core.errors.exception;

import fastcampus.scheduling._core.errors.ErrorMessage;

public class DuplicatePhoneNumberException extends RuntimeException {
    public DuplicatePhoneNumberException() {
        super(ErrorMessage.DUPLICATE_PHONENUMBER);
    }
}
