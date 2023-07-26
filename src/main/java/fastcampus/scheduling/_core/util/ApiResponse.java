package fastcampus.scheduling._core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class ApiResponse {

    private ApiResponse() {
        throw new IllegalArgumentException("Suppress default constructor");
    }

    public static <T> Result<T> success(T response) {
        return new Result<>(true, response, null);
    }

    public static Result<Object> error(String message, HttpStatus status) {
        return new Result<>(false, null, new Error(message, status.value()));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Result<T> {
        private final boolean success;
        private final T response;
        private final Error error;
    }

    @Getter @Setter @AllArgsConstructor
    public static class Error {
        private final String message;
        private final int status;
    }
}
