package com.fastcampus.scheduling._core.errors;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.INVALID_CHANGE_POSITION;

import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling._core.errors.exception.Exception403;
import com.fastcampus.scheduling._core.errors.exception.Exception404;
import com.fastcampus.scheduling._core.errors.exception.Exception500;
import com.fastcampus.scheduling._core.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<ApiResponse.Result<Object>> badRequest(Exception400 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<ApiResponse.Result<Object>> unAuthorized(Exception401 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<ApiResponse.Result<Object>> forbidden(Exception403 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<ApiResponse.Result<Object>> notFound(Exception404 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<ApiResponse.Result<Object>> serverError(Exception500 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse.Result<Object>> missingServletRequestParameterException(){
        ApiResponse.Result<Object> apiResult = ApiResponse.error(INVALID_CHANGE_POSITION, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse.Result<Object>> methodArgumentTypeMismatchException(){
        ApiResponse.Result<Object> apiResult = ApiResponse.error(INVALID_CHANGE_POSITION, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse.Result<Object>> unknownServerError(Exception e){
        ApiResponse.Result<Object> apiResult = ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
