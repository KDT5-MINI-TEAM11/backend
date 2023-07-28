package fastcampus.scheduling.jwt.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	private Long userId;
	private String refreshTokenId;

	public static RefreshToken of(Long userId, String refreshTokenId) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.userId = userId;
		refreshToken.refreshTokenId = refreshTokenId;
		return refreshToken;
	}
}
