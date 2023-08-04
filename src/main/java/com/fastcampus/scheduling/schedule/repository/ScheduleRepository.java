package com.fastcampus.scheduling.schedule.repository;

import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Schedule findByIdAndUserId(Long id, Long userId);

    List<Schedule> findAllByOrderByIdDesc();

    List<Schedule> findByUserId(Long userId);

    List<Schedule> findByUserIdAndStartDateAfter(Long userId, LocalDate startDate);

    List<Schedule> findSchedulesByStateAndStartDateBetween(State state, LocalDate startDate, LocalDate endDate);

}
