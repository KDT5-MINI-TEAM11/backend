package fastcampus.scheduling.user.service;

import static fastcampus.scheduling._core.errors.ErrorMessage.USER_NOT_FOUND;

import fastcampus.scheduling._core.errors.exception.Exception400;
import fastcampus.scheduling._core.util.HttpRequestUtil;
import fastcampus.scheduling.user.model.SigninLog;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.repository.UserLogRepository;
import fastcampus.scheduling.user.repository.UserRepository;
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
	public void saveSigninLog(String userId) {
		Map<String, String> accessInfo = HttpRequestUtil.getAccessInfo();
		User user = userRepository.findById(Long.valueOf(userId))
				.orElseThrow(() -> new Exception400(USER_NOT_FOUND));
		SigninLog signinLog = SigninLog.builder()
				.ipAddress(accessInfo.get("ip"))
				.userAgent(accessInfo.get("browser"))
				.user(user)
				.build();
		userLogRepository.save(signinLog);
	}

}
