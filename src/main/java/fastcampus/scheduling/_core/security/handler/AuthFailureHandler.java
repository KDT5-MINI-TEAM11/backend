package fastcampus.scheduling._core.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.errors.exception.InValidSigninRequestException;
import fastcampus.scheduling._core.util.ApiResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
@Configuration
public class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String message = exception.getMessage();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        if (exception instanceof InValidSigninRequestException) {
            status = HttpStatus.BAD_REQUEST;
        }
        response.setContentType("application/json");
        response.setStatus(status.value());
        new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.error(message, status));
    }
}
