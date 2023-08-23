package com.fastcampus.scheduling._core.security.filter;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_EXISTS;
import static com.fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.JwtTokenProvider;
import com.fastcampus.scheduling.jwt.service.AccessTokenServiceImpl;
import com.fastcampus.scheduling.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AccessTokenServiceImpl accessTokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<String> ignored = Arrays.asList(
            "/h2-console/**",
            "/api/v1/auth/**",
            "/api/v2/auth/**"
        );
        if (ignored.stream().anyMatch(pattern -> antPathMatcher.match(pattern, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String accessToken;
        final String userEmail;

        accessToken = accessTokenService.getAccessToken(authHeader);

        // Header에 Token이 존재 하지 않거나 비정상 적인 type
        if (accessToken.isEmpty()) {
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.error(TOKEN_NOT_EXISTS, HttpStatus.UNAUTHORIZED));
            return;
        }

        //Token이 존재 할 경우
        userEmail = jwtTokenProvider.getSubject(accessToken);
        if (userEmail == null) {
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.error(TOKEN_NOT_VALID, HttpStatus.UNAUTHORIZED));
            return;

        }

        jwtTokenProvider.setSecurityAuthentication(userEmail, accessToken);

        filterChain.doFilter(request, response);
    }
}
