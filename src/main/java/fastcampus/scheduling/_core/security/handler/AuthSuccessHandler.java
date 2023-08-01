
package fastcampus.scheduling._core.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.security.dto.SigninResponse;
import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.CookieProvider;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.service.RefreshTokenServiceImpl;
import fastcampus.scheduling.user.service.UserLogService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserLogService userLogService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        //user.getUsername will return User's id not email
        //username will overwrite at customAuthenticationProvider
        String userId = user.getUsername();
        String accessToken = jwtTokenProvider.generateJwtAccessToken(userId, request.getRequestURI(), roles);
        String refreshToken = jwtTokenProvider.generateJwtRefreshToken(userId);
        refreshTokenService.saveRefreshToken(Long.valueOf(userId), jwtTokenProvider.getRefreshTokenId(refreshToken));

        response = cookieProvider.addCookie(response, refreshToken);


        SigninResponse signinResponse = SigninResponse.builder().accessToken(accessToken).build();

        // save signIn log
        userLogService.saveSigninLog(userId);

        new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.success(signinResponse));
    }

}
