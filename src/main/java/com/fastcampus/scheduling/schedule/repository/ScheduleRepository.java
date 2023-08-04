package com.fastcampus.scheduling.schedule.repository;

import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByUserId(Long userId);
    List<Schedule> findAllByOrderByIdDesc();

    List<Schedule> findByUserIdAndStartDateAfter(Long userId, LocalDate startDate);

    @Query("SELECT s FROM Schedule s WHERE YEAR(s.startDate) = :year AND MONTH(s.startDate) = :month")
    List<Schedule> findByStartDateYearAndStartDateMonth(@Param("year") int year, @Param("month") int month);
}
