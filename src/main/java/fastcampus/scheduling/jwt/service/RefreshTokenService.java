package fastcampus.scheduling.jwt.service;

import fastcampus.scheduling.jwt.dto.RefreshAccessTokenDto;

public interface RefreshTokenService {

	void updateRefreshToken(Long id, String uuid);

	RefreshAccessTokenDto refreshAccessToken(String refreshToken);
	void revokeToken(String refreshToken);
}
