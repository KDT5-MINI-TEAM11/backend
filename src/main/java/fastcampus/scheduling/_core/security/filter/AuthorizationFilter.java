package fastcampus.scheduling._core.security.filter;

import static fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_EXISTS;
import static fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.service.AccessTokenServiceImpl;
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
    private final AccessTokenServiceImpl accessTokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<String> ignored = Arrays.asList(
            "/h2-console/**",
            "/api/v1/auth/**"
        );
        if (ignored.stream().anyMatch(pattern -> antPathMatcher.match(pattern, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String accessToken;
        final String userId;

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
        userId = jwtTokenProvider.getUserId(accessToken);
        if (userId == null) {
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.error(TOKEN_NOT_VALID, HttpStatus.UNAUTHORIZED));
            return;

        }
        jwtTokenProvider.setSecurityAuthentication(userId, accessToken);

        filterChain.doFilter(request, response);
    }
}
