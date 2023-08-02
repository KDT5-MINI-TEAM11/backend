package com.fastcampus.scheduling.schedule.repository;

import com.fastcampus.scheduling.schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
