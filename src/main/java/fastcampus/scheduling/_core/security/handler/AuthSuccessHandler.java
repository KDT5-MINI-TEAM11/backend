
package fastcampus.scheduling._core.security.handler;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.CookieProvider;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.service.RefreshTokenServiceImpl;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

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
        //username is overwrites at customAuthenticationProvider
        String userId = user.getUsername();
        String accessToken = jwtTokenProvider.generateJwtAccessToken(userId, request.getRequestURI(), roles);
        String refreshToken = jwtTokenProvider.generateJwtRefreshToken();
        refreshTokenService.updateRefreshToken(Long.valueOf(userId), jwtTokenProvider.getRefreshTokenId(refreshToken));

        ResponseCookie responseCookie = cookieProvider.generateRefreshTokenCookie(refreshToken);
        Cookie cookie = cookieProvider.of(responseCookie);
        response.addCookie(cookie);
        response.setStatus(SC_OK);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("accessToken", accessToken);

        new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.success(responseBody));
    }

}
