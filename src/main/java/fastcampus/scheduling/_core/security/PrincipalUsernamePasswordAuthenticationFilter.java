package fastcampus.scheduling._core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.errors.exception.Exception500;
import fastcampus.scheduling.user.user.dto.UserRequest;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class PrincipalUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public PrincipalUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            UserRequest.SignInDTO SignInDTO
                    = objectMapper.readValue(request.getInputStream(), UserRequest.SignInDTO.class);

            UsernamePasswordAuthenticationToken token
                    = UsernamePasswordAuthenticationToken.unauthenticated(SignInDTO.getUserEmail(), SignInDTO.getUserPassword());

            return authenticationManager.authenticate(token);

        } catch (IOException exception) {
            throw new Exception500(exception.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
