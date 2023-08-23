package com.fastcampus.scheduling.schedule.repository;

import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Schedule findByIdAndUserId(Long id, Long userId);

    List<Schedule> findAllByOrderByIdDesc();

    @Query("SELECT s FROM Schedule s WHERE s.user.id = :userId AND s.state <> 'REJECT' AND s.endDate > :startDate AND s.startDate < :endDate")
    List<Schedule> findByUserAndDatesOverlap(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM Schedule s WHERE s.id <> :id AND s.startDate < :endDate AND s.endDate > :startDate")
    List<Schedule> findByScheduleAndDatesOverlap(@Param("id") Long id, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Schedule> findSchedulesByUserIdAndStartDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Schedule> findSchedulesByStartDateBetweenAndStateNot(LocalDateTime startDate, LocalDateTime endDate, State state);

    List<Schedule> findSchedulesByUserIdAndStartDateBetweenAndState(Long userId, LocalDateTime startDate, LocalDateTime endDate, State state);

}
