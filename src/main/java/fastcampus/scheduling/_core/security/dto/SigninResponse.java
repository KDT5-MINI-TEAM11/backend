package fastcampus.scheduling._core.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninResponse {

	private String accessToken;

}
