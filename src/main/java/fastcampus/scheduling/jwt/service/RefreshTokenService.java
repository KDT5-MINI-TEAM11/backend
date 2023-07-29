package fastcampus.scheduling.jwt.service;

import fastcampus.scheduling.jwt.dto.RefreshAccessTokenDto;

public interface RefreshTokenService {

	void updateRefreshToken(String refreshToken);

	RefreshAccessTokenDto refreshAccessToken(String refreshToken);

	void revokeToken(String refreshToken);

	void saveRefreshToken(Long id, String uuid);
}
