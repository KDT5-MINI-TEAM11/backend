package fastcampus.scheduling._core.security;

import fastcampus.scheduling._core.errors.ErrorMessage;
import fastcampus.scheduling._core.errors.exception.Exception401;
import fastcampus.scheduling._core.errors.exception.Exception403;
import fastcampus.scheduling._core.util.FilterResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new PrincipalAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()

            .headers().frameOptions().sameOrigin()  // iframe disable

            .and().cors().configurationSource(configurationSource()) // cors setting

            .and().formLogin()
//            .loginPage("/loginForm")
//            .loginProcessingUrl("/login")
            .loginPage("/")
            .loginProcessingUrl("/")
            .defaultSuccessUrl("/")
            .failureHandler(authenticationFailureHandler())
            .permitAll()

            .and().httpBasic().disable()

            .apply(new SecurityFilterManager())

            .and().exceptionHandling()
            .authenticationEntryPoint((request, response, authException) ->
                FilterResponse.unAuthorized(response, new Exception401(ErrorMessage.UN_AUTHORIZED)))

            .and().exceptionHandling()
            .accessDeniedHandler((request, response, accessDeniedException) ->
                FilterResponse.forbidden(response, new Exception403(ErrorMessage.FORBIDDEN)))

            .and().authorizeRequests()
            .antMatchers("/api/v1/auth/signup").permitAll() // 회원가입은 모두 허용
            .antMatchers("", "").authenticated() // 인증된 사용자만 접근 가능
            .antMatchers("/LEVEL/**").hasRole("LEVEL1")
            .antMatchers("/intern/**").hasRole("INTERN")
            .anyRequest().permitAll(); // 나머지 요청은 모두 허용

        return http.build();
    }

    private CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    public static class SecurityFilterManager extends
        AbstractHttpConfigurer<SecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(
                AuthenticationManager.class);
            builder.addFilter(
                new PrincipalUsernamePasswordAuthenticationFilter(authenticationManager));
            super.configure(builder);
        }
    }
}
