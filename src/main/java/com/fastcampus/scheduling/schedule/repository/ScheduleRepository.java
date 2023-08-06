package com.fastcampus.scheduling.schedule.repository;

import com.fastcampus.scheduling.schedule.common.State;
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

    @Query("SELECT s FROM Schedule s WHERE s.user.id = :userId AND s.startDate >= :startDate AND s.endDate <= :endDate")
    List<Schedule> findByUserIdAndYear(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT s FROM Schedule s WHERE s.startDate >= :startDate AND s.endDate <= :endDate")
    List<Schedule> findAllByYear(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}
