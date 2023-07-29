package fastcampus.scheduling.jwt.service;

import static fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;
import static fastcampus.scheduling._core.errors.ErrorMessage.USER_NOT_FOUND;

import fastcampus.scheduling._core.errors.exception.Exception401;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.dto.RefreshAccessTokenDto;
import fastcampus.scheduling.jwt.model.RefreshToken;
import fastcampus.scheduling.jwt.repository.RefreshTokenRepository;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.repository.UserRepository;
import fastcampus.scheduling.user.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserService userService;

	@Override
	public void updateRefreshToken(String refreshToken) {
		Long userId = Long.valueOf(jwtTokenProvider.getUserId(refreshToken));
		RefreshToken findRefreshToken = findRefreshToken(userId, refreshToken);
		findRefreshToken.updateRefreshTokenId(String.valueOf(UUID.randomUUID()));
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
	public RefreshAccessTokenDto refreshAccessToken(String refreshToken) {
		try {
			String userId = jwtTokenProvider.getUserId(refreshToken);

			User findUser = userRepository.findById(Long.valueOf(userId))
					.orElseThrow(() -> new Exception401(
							"User Id : " + userId + " " + USER_NOT_FOUND));

			jwtTokenProvider.validateJwtToken(refreshToken);
			updateRefreshToken(refreshToken);

			Authentication authentication = getAuthentication(findUser.getUserEmail());
			List<String> roles = authentication.getAuthorities()
					.stream().map(GrantedAuthority::getAuthority).toList();

			String newAccessToken = jwtTokenProvider.generateJwtAccessToken(userId, "/refresh-token",
					roles);

			return RefreshAccessTokenDto.builder()
					.accessToken(newAccessToken)
					.build();
		} catch (Exception exception) {
			throw new Exception401(TOKEN_NOT_VALID);
		}

	}

	@Transactional
	@Override
	public void revokeToken(String refreshToken) {
		String userId = jwtTokenProvider.getUserId(refreshToken);
		refreshTokenRepository.deleteByUserId(Long.valueOf(userId));
	}

	public Authentication getAuthentication(String email) {
		UserDetails userDetails = userService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
	}

	public RefreshToken findRefreshToken(Long userId, String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findByUserId(userId)
				.orElseThrow(
						() -> new Exception401(TOKEN_NOT_VALID));

		String findRefreshTokenId = findRefreshToken.getRefreshTokenId();
		if (!jwtTokenProvider.equalRefreshTokenId(findRefreshTokenId, refreshToken)) {
			throw new Exception401(TOKEN_NOT_VALID);
		}
		return findRefreshToken;
	}
}
