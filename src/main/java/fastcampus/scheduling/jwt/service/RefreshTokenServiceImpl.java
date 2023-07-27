package fastcampus.scheduling.jwt.service;

import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.jwt.dto.JwtTokenDto;
import fastcampus.scheduling.jwt.exception.JwtExceptionMessage;
import fastcampus.scheduling.jwt.exception.UnauthorizedException;
import fastcampus.scheduling.jwt.model.RefreshToken;
import fastcampus.scheduling.jwt.repository.RefreshTokenRepository;
import fastcampus.scheduling.user.exception.UserExceptionMessage;
import fastcampus.scheduling.user.exception.UserNotExistException;
import fastcampus.scheduling.user.user.model.User;
import fastcampus.scheduling.user.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final UserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	@Override
	public void updateRefreshToken(Long id, String uuid) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotExistException("User Id : " + id + " " + UserExceptionMessage.USER_NOT_FOUND_EXCEPTION));

		refreshTokenRepository.save(RefreshToken.of(user.getUserEmail(), uuid));
	}

	@Override
	public JwtTokenDto refreshAccessToken(String refreshToken) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		revokeToken(userId, refreshToken);


		User findUser = userRepository.findById(Long.valueOf(userId))
				.orElseThrow(() -> new UserNotExistException(
						"User Id : " + userId + " " + UserExceptionMessage.USER_NOT_FOUND_EXCEPTION.getMessage()));

		Authentication authentication = getAuthentication(findUser.getUserEmail());
		List<String> roles = authentication.getAuthorities()
				.stream().map(GrantedAuthority::getAuthority).toList();

		String newAccessToken = jwtTokenProvider.generateJwtAccessToken(userId, "/refresh-token", roles);

		return JwtTokenDto.builder()
				.accessToken(newAccessToken)
				.accessTokenExpiredDate(jwtTokenProvider.getExpiredTime(newAccessToken))
				.refreshToken(refreshToken)
				.build();
	}

	@Transactional
	@Override
	public void revokeToken(String userId, String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findById(userId)
				.orElseThrow(
						() -> new UnauthorizedException(HttpStatus.UNAUTHORIZED, JwtExceptionMessage.TOKEN_NOT_VALID.getMessage()));

		String findRefreshTokenId = findRefreshToken.getRefreshTokenId();
		if (!jwtTokenProvider.equalRefreshTokenId(findRefreshTokenId, refreshToken)) {
			throw new UnauthorizedException(HttpStatus.UNAUTHORIZED, JwtExceptionMessage.TOKEN_NOT_VALID.getMessage());
		}
		refreshTokenRepository.delete(findRefreshToken);
	}

	public Authentication getAuthentication(String email) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
	}
}
