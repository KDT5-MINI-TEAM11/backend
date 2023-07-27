package fastcampus.scheduling.jwt.repository;

import fastcampus.scheduling.jwt.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
