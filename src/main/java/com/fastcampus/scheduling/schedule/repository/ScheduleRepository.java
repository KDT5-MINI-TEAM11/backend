package com.fastcampus.scheduling.schedule.repository;

import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Schedule findByIdAndUserId(Long id, Long userId);

    List<Schedule> findAllByOrderByIdDesc();

    @Query("SELECT s FROM Schedule s WHERE s.user.id = :userId AND s.endDate > :startDate AND s.startDate < :endDate")
    List<Schedule> findByUserAndDatesOverlap(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Schedule> findSchedulesByUserIdAndStartDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Schedule> findSchedulesByStartDateBetween(LocalDate startDate, LocalDate endDate);


}
