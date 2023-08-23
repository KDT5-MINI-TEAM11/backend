package com.fastcampus.scheduling.jwt.repository;

import com.fastcampus.scheduling.jwt.model.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByUserId(Long userId);

	void deleteByUserId(Long userId);

}
