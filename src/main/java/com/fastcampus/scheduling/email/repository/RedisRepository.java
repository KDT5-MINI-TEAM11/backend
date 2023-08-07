package com.fastcampus.scheduling.email.repository;

import com.fastcampus.scheduling.email.dto.EmailRequest.CheckEmailAuthDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<CheckEmailAuthDTO, String> {

}
