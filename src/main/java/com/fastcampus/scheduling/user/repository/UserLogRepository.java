package com.fastcampus.scheduling.user.repository;

import com.fastcampus.scheduling.user.model.SigninLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<SigninLog, Long> {

}
