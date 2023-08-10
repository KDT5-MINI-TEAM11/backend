package com.fastcampus.scheduling._core.security.filter;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.MISMATCH_SIGN_IN_INFO;

import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling.user.service.UserService;
import java.nio.charset.StandardCharsets;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Resource
    private final UserService userService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        String userEmail = (String) authenticationToken.getPrincipal();
        String userPassword = (String) authenticationToken.getCredentials();

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) userService.loadUserByUsername(userEmail);
        log.info("Before Compare Password");
        log.info("authenticate UserID : " + user.getUsername());
        log.info("authenticate UserPassword : " + user.getPassword());

        byte[] passworDb = userPassword.getBytes(StandardCharsets.UTF_8);
        CustomBCrypt.checkpw(passworDb, user.getPassword());
        if (!passwordEncoder.matches(userPassword, user.getPassword())) {
            log.warn("password Not Match From matches");
            throw new Exception400(MISMATCH_SIGN_IN_INFO);
        }
        // set credentials null
        // credentials will remove by spring
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
