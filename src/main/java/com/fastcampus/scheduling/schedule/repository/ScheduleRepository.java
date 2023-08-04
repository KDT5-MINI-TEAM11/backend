package com.fastcampus.scheduling.schedule.repository;

import com.fastcampus.scheduling.schedule.model.Schedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByUserId(Long userId);
    List<Schedule> findAllByOrderByIdDesc();

    List<Schedule> findByScheduleId(Long userId);

}
