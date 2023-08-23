package com.fastcampus.scheduling._core.security.dto;

import lombok.Getter;

@Getter
public class SignInRequest {

	private String userEmail;
	private String userPassword;
}
