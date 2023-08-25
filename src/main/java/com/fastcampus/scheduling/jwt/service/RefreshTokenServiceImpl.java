package com.fastcampus.scheduling.jwt.service;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.INVALID_TOKEN_REFRESH_REQUEST;
import static com.fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;

import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling._core.util.JwtTokenProvider;
import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenRequestDto;
import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenResponseDto;
import com.fastcampus.scheduling.jwt.model.RefreshToken;
import com.fastcampus.scheduling.jwt.repository.RefreshTokenRepository;
import com.fastcampus.scheduling.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	@Override
	public void updateRefreshToken(User user, String refreshToken, String newRefreshToken) {
		validateRefreshToken(user, refreshToken);
		RefreshToken findRefreshToken = findRefreshToken(user.getId(), refreshToken);
		String newRefreshTokenId = jwtTokenProvider.getRefreshTokenId(newRefreshToken);
		findRefreshToken.updateRefreshTokenId(newRefreshTokenId);
		refreshTokenRepository.save(findRefreshToken);
	}

	private void validateRefreshToken(User user, String refreshToken) {
		String userEmail = jwtTokenProvider.getSubject(refreshToken);
		if (!userEmail.equals(user.getUserEmail()) || userEmail.isEmpty()) {
			throw new Exception401(INVALID_TOKEN_REFRESH_REQUEST);
		}
	}

	@Transactional
	@Override
	public void saveRefreshToken(Long id, String uuid) {
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(id).orElse(RefreshToken.of(id, uuid));
		if(refreshToken.getId() != null) {
			refreshToken.updateRefreshTokenId(uuid);
		}
		refreshTokenRepository.save(refreshToken);
	}

	@Override
	public RefreshAccessTokenResponseDto refreshAccessToken(String accessToken) {
		return RefreshAccessTokenResponseDto.builder()
				.accessToken(accessToken)
				.build();
	}

	@Transactional
	@Override
	public void revokeToken(String refreshToken) {
		String userId = jwtTokenProvider.getSubject(refreshToken);
		refreshTokenRepository.deleteByUserId(Long.valueOf(userId));
	}

	public RefreshToken findRefreshToken(Long userId, String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findByUserId(userId)
				.orElseThrow(
						() -> new Exception401(TOKEN_NOT_VALID));

		String findRefreshTokenId = findRefreshToken.getRefreshTokenId();
		if (jwtTokenProvider.isNotEqualRefreshTokenId(findRefreshTokenId, refreshToken)) {
			throw new Exception401(TOKEN_NOT_VALID);
		}
		return findRefreshToken;
	}

	@Override
	public String getRefreshToken(RefreshAccessTokenRequestDto request) {

		String refreshToken = request.getRefreshToken();

		jwtTokenProvider.validateJwtToken(refreshToken);

		return refreshToken;
	}
}
