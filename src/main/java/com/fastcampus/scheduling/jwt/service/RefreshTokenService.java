package com.fastcampus.scheduling.jwt.service;

import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenRequestDto;
import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenResponseDto;
import com.fastcampus.scheduling.user.model.User;

public interface RefreshTokenService {

	void updateRefreshToken(User user, String refreshToken, String newRefreshToken);

	RefreshAccessTokenResponseDto refreshAccessToken(String refreshToken);

	void revokeToken(String refreshToken);

	void saveRefreshToken(Long id, String uuid);

	String getRefreshToken(RefreshAccessTokenRequestDto request);
}
