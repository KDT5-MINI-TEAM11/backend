
package com.fastcampus.scheduling._core.security.handler;

import com.fastcampus.scheduling._core.security.dto.SignInResponse;
import com.fastcampus.scheduling._core.security.dto.UserPrincipal;
import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.CookieProvider;
import com.fastcampus.scheduling._core.util.JwtTokenProvider;
import com.fastcampus.scheduling.jwt.service.RefreshTokenServiceImpl;
import com.fastcampus.scheduling.user.service.UserLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class AuthSuccessHandlerForLocal implements AuthenticationSuccessHandler {

    private final UserLogService userLogService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        String userEmail = userDetails.getUsername();
        Long userId = userDetails.getUser().getId();
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();

        String accessToken = jwtTokenProvider.generateJwtAccessToken(userEmail, request.getRequestURI(), authorities);
        String refreshToken = jwtTokenProvider.generateJwtRefreshToken(userEmail);
        refreshTokenService.saveRefreshToken(userId, jwtTokenProvider.getRefreshTokenId(refreshToken));

        response = cookieProvider.addCookie(response, refreshToken);


        SignInResponse signinResponse = SignInResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();

        // save signIn log
        userLogService.saveSignInLog(userDetails.getUser());

        new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.success(signinResponse));
    }

}
