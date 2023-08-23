package com.fastcampus.scheduling.user.service;

import com.fastcampus.scheduling._core.util.HttpRequestUtil;
import com.fastcampus.scheduling.user.model.SigninLog;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserLogRepository;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLogService {

  private final UserLogRepository userLogRepository;
	private final UserRepository userRepository;


	@Transactional
	public void saveSignInLog(User user) {
		Map<String, String> accessInfo = HttpRequestUtil.getAccessInfo();

		SigninLog signinLog = SigninLog.builder()
				.ipAddress(accessInfo.get("ip"))
				.userAgent(accessInfo.get("browser"))
				.user(user)
				.build();
		userLogRepository.save(signinLog);
	}

}
