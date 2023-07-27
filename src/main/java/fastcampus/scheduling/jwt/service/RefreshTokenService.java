package fastcampus.scheduling.jwt.service;

import fastcampus.scheduling.jwt.dto.JwtTokenDto;

public interface RefreshTokenService {

	void updateRefreshToken(Long id, String uuid);

	JwtTokenDto refreshAccessToken(String refreshToken);
	void revokeToken(String userId, String refreshToken);
}
