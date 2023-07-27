package fastcampus.scheduling._core.security.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.security.exception.AuthExceptionMessage;
import fastcampus.scheduling._core.security.dto.SigninRequest;
import fastcampus.scheduling._core.security.exception.FieldMissingException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken;
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        authenticationToken = generateAuthenticationToken(request);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken generateAuthenticationToken(HttpServletRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            SigninRequest signinRequest = objectMapper.readValue(request.getInputStream(), SigninRequest.class);
            return new UsernamePasswordAuthenticationToken(signinRequest.getUserEmail(), signinRequest.getUserPassword());
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new FieldMissingException(AuthExceptionMessage.INVALID_REQUEST.getMessage());
        }

    }

}
