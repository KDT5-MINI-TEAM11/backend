package com.fastcampus.scheduling._core.security.filter;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.MISMATCH_SIGN_IN_INFO;

import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling.user.service.UserService;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Resource
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        String userEmail = (String) authenticationToken.getPrincipal();
        String userPassword = (String) authenticationToken.getCredentials();

        UserDetails userDetails = userService.loadUserByUsername(userEmail);

        if (!passwordEncoder.matches(userPassword, userDetails.getPassword())) {
            log.warn("password Not Match From matches");
            throw new Exception400(MISMATCH_SIGN_IN_INFO);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
