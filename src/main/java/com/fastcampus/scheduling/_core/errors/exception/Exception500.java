package com.fastcampus.scheduling._core.errors.exception;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * internal server error exception
 */
@Getter
public class Exception500 extends RuntimeException {

    public Exception500(String message) {
        super(message);
    }

    public Result<Object> body() {
        return ApiResponse.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
