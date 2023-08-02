package com.fastcampus.scheduling.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshAccessTokenDto {
	private String accessToken;
	public RefreshAccessTokenDto(String accessToken) {
		this.accessToken = accessToken;
	}
}
