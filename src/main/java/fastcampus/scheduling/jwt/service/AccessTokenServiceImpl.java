package fastcampus.scheduling.jwt.service;

import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.exception.JwtExceptionMessage;
import fastcampus.scheduling.jwt.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public String getAccessToken(String authorizationHeader) {

		String accessToken = authorizationHeader.replace("Bearer ", "");

		if (jwtTokenProvider.validateJwtToken(accessToken)) {
			throw new UnauthorizedException(HttpStatus.UNAUTHORIZED, JwtExceptionMessage.TOKEN_NOT_VALID.getMessage());
		}
		return accessToken;
	}
}
