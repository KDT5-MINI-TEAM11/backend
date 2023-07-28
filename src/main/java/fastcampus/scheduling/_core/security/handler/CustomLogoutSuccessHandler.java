package fastcampus.scheduling._core.security.handler;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.scheduling._core.util.ApiResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
@Configuration
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		response.setStatus(SC_OK);
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("utf-8");

		new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.success("로그아웃이 완료 되었습니다."));
	}
}
