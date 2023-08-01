package fastcampus.scheduling.jwt.service;

import static fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;

import fastcampus.scheduling._core.errors.exception.Exception401;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public String getAccessToken(String authorizationHeader) {

		if (authorizationHeader == null) {
			throw new Exception401(TOKEN_NOT_VALID);
		}
		String accessToken = authorizationHeader.replace("Bearer ", "");

		jwtTokenProvider.validateJwtToken(accessToken);

		return accessToken;
	}
}
