package com.fastcampus.scheduling._core.errors.exception;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * not found exception
 */
@Getter
public class Exception404 extends RuntimeException {

    public Exception404(String message) {
        super(message);
    }

    public Result<Object> body() {
        return ApiResponse.error(getMessage(), HttpStatus.NOT_FOUND);
    }

    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}