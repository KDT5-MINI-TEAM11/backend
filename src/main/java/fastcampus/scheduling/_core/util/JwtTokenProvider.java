package fastcampus.scheduling._core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {
	@Value("${jwt.access-token.expiration}")
	private long ACCESS_EXPIRED_TIME;

	@Value("${jwt.refresh-token.expiration}")
	private long REFRESH_EXPIRED_TIME;

	@Value("${jwt.secret-key}")
	private String SECRET;

	public String generateJwtAccessToken(String userId, String uri, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("roles", roles);

		return buildToken(claims, uri, ACCESS_EXPIRED_TIME);
	}

	public String generateJwtRefreshToken() {
		Claims claims = Jwts.claims();
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

	public Date getExpiredTime(String token) {
		return getClaimsFromJwtToken(token).getExpiration();
	}

	public List<String> getRoles(String token) {
		return (List<String>) getClaimsFromJwtToken(token).get("roles");
	}
	public boolean validateJwtToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			return false;
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
			return false;
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
			return false;
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
}