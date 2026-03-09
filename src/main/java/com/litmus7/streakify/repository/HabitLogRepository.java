package com.litmus7.streakify.repository;

import com.litmus7.streakify.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog,Long> {

    Optional<HabitLog> findByHabitIdAndLogDate(Long habitId, LocalDate logDate);
    List<HabitLog> findByHabitId(Long habitId);
    List<HabitLog> findByHabitIdOrderByLogDateDesc(Long habitId);
    //List<HabitLog> findByHabitIdOrderByLogDateAsc(Long habitId);

}
