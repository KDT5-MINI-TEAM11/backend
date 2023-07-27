package fastcampus.scheduling._core.security.dto;

import lombok.Getter;

@Getter
public class SigninRequest {

	private String userEmail;
	private String userPassword;
}
