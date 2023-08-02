package com.fastcampus.scheduling.jwt.service;

import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenDto;

public interface RefreshTokenService {

	void updateRefreshToken(String userId, String refreshToken, String newRefreshToken);

	RefreshAccessTokenDto refreshAccessToken(String refreshToken);

	void revokeToken(String refreshToken);

	void saveRefreshToken(Long id, String uuid);
}
