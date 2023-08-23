package com.fastcampus.scheduling._core.security.filter;

import com.fastcampus.scheduling._core.errors.exception.FieldMissExceptionException;
import com.fastcampus.scheduling._core.security.dto.SignInRequest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class AuthenticationFilterForLocal extends UsernamePasswordAuthenticationFilter {

    public AuthenticationFilterForLocal(AuthenticationManager authenticationManager) {
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
            SignInRequest signinRequest = objectMapper.readValue(request.getInputStream(), SignInRequest.class);
            String userEmail = signinRequest.getUserEmail();
            String userPassword = signinRequest.getUserPassword();

            if (userEmail == null || userPassword == null) {
                throw new FieldMissExceptionException();
            }
            return new UsernamePasswordAuthenticationToken(userEmail, userPassword);
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new FieldMissExceptionException();
        }

    }

}
