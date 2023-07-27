package fastcampus.scheduling._core.security.filter;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.exception.JwtExceptionMessage;
import fastcampus.scheduling.jwt.service.AccessTokenServiceImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
                "/api/v1/auth/signin",
                "/api/v1/auth/signout"
        );
        if (ignored.stream().anyMatch(pattern -> antPathMatcher.match(pattern, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String accessToken;
        final String userId;


        // Header에 Token이 존재 하지 않거나 비정상 적인 type
        if (authHeader == null || accessTokenService.getAccessToken(authHeader).equals("")) {
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.error(JwtExceptionMessage.TOKEN_NOT_VALID.getMessage(), HttpStatus.UNAUTHORIZED));
            return;
        }

        //Token이 존재 할 경우
        accessToken = accessTokenService.getAccessToken(authHeader);
        userId = jwtTokenProvider.getUserId(accessToken);
        if (userId == null) {
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.error(JwtExceptionMessage.TOKEN_NOT_VALID.getMessage(), HttpStatus.UNAUTHORIZED));
            return;

        }
        List<String> roles = jwtTokenProvider.getRoles(accessToken);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        //Set Authentication to SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
