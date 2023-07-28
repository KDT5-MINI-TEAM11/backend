package fastcampus.scheduling._core.errors;

import fastcampus.scheduling._core.errors.exception.Exception400;
import fastcampus.scheduling._core.errors.exception.Exception401;
import fastcampus.scheduling._core.errors.exception.Exception403;
import fastcampus.scheduling._core.errors.exception.Exception404;
import fastcampus.scheduling._core.errors.exception.Exception500;
import fastcampus.scheduling._core.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse.Result<Object>> unknownServerError(Exception e){
        ApiResponse.Result<Object> apiResult = ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
