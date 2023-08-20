package com.fastcampus.scheduling._core.util;

import static com.fastcampus.scheduling._core.errors.ErrorMessage.INNER_SERVER_ERROR;
import static com.fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_EXPIRED;
import static com.fastcampus.scheduling._core.errors.ErrorMessage.TOKEN_NOT_VALID;
import static com.fastcampus.scheduling._core.errors.ErrorMessage.UN_AUTHORIZED;

import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling._core.errors.exception.Exception500;
import com.fastcampus.scheduling.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	@Value("${jwt.access-token.expiration}")
	private long ACCESS_EXPIRED_TIME;

	@Value("${jwt.refresh-token.expiration}")
	private long REFRESH_EXPIRED_TIME;

	@Value("${jwt.secret-key}")
	private String SECRET;

	private final UserService userService;

	public String generateJwtAccessToken(String userId, String uri, Collection<GrantedAuthority> roles) {
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("roles", roles);

		return buildToken(claims, uri, ACCESS_EXPIRED_TIME);
	}

	public org.springframework.security.core.userdetails.User getUserDetail(com.fastcampus.scheduling.user.model.User user) {
		String userEmail = user.getUserEmail();
		return (org.springframework.security.core.userdetails.User) userService.loadUserByUsername(userEmail);
	}

	public String generateJwtRefreshToken(String userId) {
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("value", UUID.randomUUID());

		return buildToken(claims, "", REFRESH_EXPIRED_TIME);
	}

	public String getUserId(String token) {
		return getClaimsFromJwtToken(token).getSubject();
	}

	private Claims getClaimsFromJwtToken(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public String getRefreshTokenId(String token) {
		return getClaimsFromJwtToken(token).get("value").toString();
	}

	public List<LinkedHashMap> getRoles(String token) {
		return (List<LinkedHashMap>) getClaimsFromJwtToken(token).get("roles");
	}
	public void validateJwtToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
			return;
		} catch (ExpiredJwtException exception) {
			log.error("JWT token is expired: {}", exception.getMessage());
			throw new Exception401(TOKEN_EXPIRED);
		} catch (JwtException exception) {
			throw new Exception401(TOKEN_NOT_VALID);
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
			throw new Exception401(TOKEN_NOT_VALID);
		} catch (Exception exception) {
			log.error("Unexpected Error at validateJwtToken");
			throw new Exception500(INNER_SERVER_ERROR);
		}
	}

	public boolean equalRefreshTokenId(String refreshTokenId, String refreshToken) {
		String compareToken = this.getRefreshTokenId(refreshToken);
		return refreshTokenId.equals(compareToken);
	}

	private Key getSignInKey() {
		byte[] keyBytes =  Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private String buildToken(Claims claims, String uri, long expiration) {
		return Jwts.builder()
				.addClaims(claims)
				.setExpiration(
						new Date(System.currentTimeMillis() + expiration)
				)
				.setIssuedAt(new Date())
				.signWith(getSignInKey(), SignatureAlgorithm.HS512)
				.setIssuer(uri)
				.compact();
	}

	public void setSecurityAuthentication(String userId, String accessToken) {
		if (userId == null || accessToken.isEmpty()) {
			throw new Exception401(UN_AUTHORIZED);
		}
		List<LinkedHashMap> roles = getRoles(accessToken);
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (LinkedHashMap role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.get("authority").toString()));
		}

		Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
		//Set Authentication to SecurityContextHolder
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
