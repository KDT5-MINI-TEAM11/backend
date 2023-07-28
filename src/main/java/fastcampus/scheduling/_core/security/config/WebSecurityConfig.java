package fastcampus.scheduling._core.security.config;

import fastcampus.scheduling._core.security.filter.AuthenticationFilter;
import fastcampus.scheduling._core.security.filter.AuthorizationFilter;
import fastcampus.scheduling._core.security.filter.CustomAuthenticationProvider;
import fastcampus.scheduling._core.security.handler.AuthFailureHandler;
import fastcampus.scheduling._core.security.handler.AuthSuccessHandler;
import fastcampus.scheduling._core.security.handler.CustomExceptionHandler;
import fastcampus.scheduling._core.security.handler.CustomLogoutHandler;
import fastcampus.scheduling._core.security.handler.CustomLogoutSuccessHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final AuthSuccessHandler loginSuccessHandler;
	private final AuthFailureHandler loginFailureHandler;
	private final CustomLogoutHandler customLogoutHandler;
	private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
	private final CustomAuthenticationProvider customAuthenticationProvider;
	private final AuthorizationFilter authorizationFilter;
	private final CustomExceptionHandler customExceptionHandler;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception  {
		http.csrf().disable()
				.cors().configurationSource(corsConfigurationSource())
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(
						"/h2-console/**",
						"/api/v1/auth/siginin",
						"/api/v1/auth/refresh-token"
				)
				.permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().disable()
				.logout()
				.logoutUrl("/api/v1/auth/signout")
				.addLogoutHandler(customLogoutHandler)
				.deleteCookies()
				.logoutSuccessHandler(customLogoutSuccessHandler)
				.and()
				.addFilterBefore(authorizationFilter, BasicAuthenticationFilter.class)
				.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(customExceptionHandler, AuthorizationFilter.class)
				.headers()
				.frameOptions()
				.sameOrigin();


		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(customAuthenticationProvider);
	}

	@Bean
	public AuthenticationFilter authenticationFilter() {
		AuthenticationFilter customAuthenticationFilter = new AuthenticationFilter(authenticationManager());
		customAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/signin");     // 접근 URL
		customAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);    // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
		customAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);    // '인증' 실패 시 해당 핸들러로 처리를 전가한다.
		customAuthenticationFilter.afterPropertiesSet();
		return customAuthenticationFilter;
	}

	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));
		configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type")); // Add other allowed headers as needed
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Add other allowed methods as needed
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		//allow cors all url
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}
