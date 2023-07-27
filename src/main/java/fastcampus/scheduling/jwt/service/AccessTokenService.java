package fastcampus.scheduling.jwt.service;

public interface AccessTokenService {

	String getAccessToken(String authorizationHeader);
}
