package com.fastcampus.scheduling.jwt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private Long userId;
	private String refreshTokenId;

	public static RefreshToken of(Long userId, String refreshTokenId) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.userId = userId;
		refreshToken.refreshTokenId = refreshTokenId;
		return refreshToken;
	}

	public void updateRefreshTokenId(String refreshTokenId) {
		this.refreshTokenId = refreshTokenId;
	}
}
