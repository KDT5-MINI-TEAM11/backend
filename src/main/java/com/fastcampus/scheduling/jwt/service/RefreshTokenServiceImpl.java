package com.fastcampus.scheduling.jwt.service;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;

import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling._core.util.JwtTokenProvider;
import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenRequestDto;
import com.fastcampus.scheduling.jwt.dto.RefreshAccessTokenResponseDto;
import com.fastcampus.scheduling.jwt.model.RefreshToken;
import com.fastcampus.scheduling.jwt.repository.RefreshTokenRepository;
import java.util.Optional;
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
	public void updateRefreshToken(String userId, String refreshToken, String newRefreshToken) {
		RefreshToken findRefreshToken = findRefreshToken(userId, refreshToken);
		String newRefreshTokenId = jwtTokenProvider.getRefreshTokenId(newRefreshToken);
		findRefreshToken.updateRefreshTokenId(newRefreshTokenId);
		refreshTokenRepository.save(findRefreshToken);
	}

	@Transactional
	@Override
	public void saveRefreshToken(Long id, String uuid) {
		Optional<RefreshToken> findRefreshTokenOpt = refreshTokenRepository.findByUserId(id);
		if (findRefreshTokenOpt.isEmpty()) {
			refreshTokenRepository.save(RefreshToken.of(id, uuid));
			return;
		}
		RefreshToken findRefreshToken = findRefreshTokenOpt.get();
		findRefreshToken.updateRefreshTokenId(uuid);
		refreshTokenRepository.save(findRefreshToken);
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

	public RefreshToken findRefreshToken(String userId, String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findByUserId(Long.valueOf(userId))
				.orElseThrow(
						() -> new Exception401(TOKEN_NOT_VALID));

		String findRefreshTokenId = findRefreshToken.getRefreshTokenId();
		if (!jwtTokenProvider.isEqualRefreshTokenId(findRefreshTokenId, refreshToken)) {
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
