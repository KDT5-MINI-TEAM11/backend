package com.fastcampus.scheduling.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshAccessTokenResponseDto {
	private String accessToken;
	public RefreshAccessTokenResponseDto(String accessToken) {
		this.accessToken = accessToken;
	}
}
