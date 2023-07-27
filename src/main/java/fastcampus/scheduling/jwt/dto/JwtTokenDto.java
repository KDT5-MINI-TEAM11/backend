package fastcampus.scheduling.jwt.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenDto {
	private String accessToken;
	private Date accessTokenExpiredDate;
	private String refreshToken;

	public JwtTokenDto(String accessToken, Date accessTokenExpiredDate, String refreshToken) {
		this.accessToken = accessToken;
		this.accessTokenExpiredDate = accessTokenExpiredDate;
		this.refreshToken = refreshToken;
	}
}
