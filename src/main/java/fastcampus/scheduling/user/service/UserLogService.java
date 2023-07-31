package fastcampus.scheduling.user.service;

import fastcampus.scheduling._core.util.HttpRequestUtil;
import fastcampus.scheduling.user.model.SigninLog;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.repository.UserLogRepository;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogService {

  private final UserLogRepository userLogRepository;

	public void saveSigninLog(HttpServletRequest request) {
		String ipAddress = HttpRequestUtil.getClientIpAddressIfServletRequestExist(request);
		SigninLog signinLog = SigninLog.builder()
				.ipAddress(ipAddress)
				.userAgent()

				.build();
		userLogRepository.save()
	}

}
