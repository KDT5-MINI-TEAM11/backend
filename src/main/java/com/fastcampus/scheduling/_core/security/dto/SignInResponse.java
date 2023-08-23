package com.fastcampus.scheduling._core.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInResponse {

	private String accessToken;
	//temp var for cross domain
	private String refreshToken;

}
