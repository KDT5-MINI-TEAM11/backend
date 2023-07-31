package fastcampus.scheduling._core.errors.exception;

import fastcampus.scheduling._core.util.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Valid 어노테이션을 통한 유효성 검사 실패, 잘못된 파라미터 요청에 대한 Exception
 */
@Getter
public class Exception400 extends RuntimeException {

    public Exception400(String message) {
        super(message);
    }


    public ApiResponse.Result<Object> body(){
        return ApiResponse.error(getMessage(), HttpStatus.BAD_REQUEST);
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}