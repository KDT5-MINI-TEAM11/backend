package fastcampus.scheduling._core.security.filter;

import fastcampus.scheduling._core.security.exception.AuthExceptionMessage;
import fastcampus.scheduling.user.service.UserService;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
        if (!passwordEncoder.matches(userPassword, user.getPassword())) {
            throw new BadCredentialsException(AuthExceptionMessage.MISMATCH_SIGN_IN_INFO.getMessage());
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
