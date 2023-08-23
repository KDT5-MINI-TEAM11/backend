package com.fastcampus.scheduling.jwt.service;

import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenRequestDto;
import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenResponseDto;

public interface RefreshTokenService {

	void updateRefreshToken(String userId, String refreshToken, String newRefreshToken);

	RefreshAccessTokenResponseDto refreshAccessToken(String refreshToken);

	void revokeToken(String refreshToken);

	void saveRefreshToken(Long id, String uuid);

	String getRefreshToken(RefreshAccessTokenRequestDto request);
}
