package com.litmus7.streakify.service;

import com.litmus7.streakify.dto.HabitLogRequestDTO;
import com.litmus7.streakify.dto.LogResponseDTO;
import com.litmus7.streakify.dto.StreakResponseDTO;
import com.litmus7.streakify.entity.Habit;
import com.litmus7.streakify.entity.HabitLog;
import com.litmus7.streakify.exception.BadRequestException; // You need to create this!
import com.litmus7.streakify.exception.ResourceNotFoundException;
import com.litmus7.streakify.repository.HabitLogRepository;
import com.litmus7.streakify.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class HabitLogService {
    @Autowired
    private HabitLogRepository habitLogRepository;

    @Autowired
    private HabitRepository habitRepository;

    public LogResponseDTO logHabit( Long habitId, HabitLogRequestDTO logRequest) {
        // 1. Fetch Habit
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found with id : " + habitId));

        // 3. Validation: Past Date Safety (No logging before creation)
        if (logRequest.getLogDate().isBefore(habit.getCreatedAt().toLocalDate())) {
            throw new BadRequestException("Cannot log for a date before the habit was created on: " + habit.getCreatedAt().toLocalDate());
        }

        // 4. Validation: Future Date Safety
        if (logRequest.getLogDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Cannot log for future dates");
        }

        // 5. Validation: Duplicate Log Check
        habitLogRepository.findByHabitIdAndLogDate(habitId, logRequest.getLogDate())
                .ifPresent(log -> {
                    throw new BadRequestException("Habit already logged for this date: " + logRequest.getLogDate());
                });

        // 6. Save the Log
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setLogDate(logRequest.getLogDate());
        habitLog.setCompleted(logRequest.isCompleted());
        HabitLog savedLog = habitLogRepository.save(habitLog);

        // 7. Calculate Weekly Progress for Notification
        LocalDate startOfWeek = logRequest.getLogDate().with(java.time.DayOfWeek.MONDAY);
        long weeklyCount = habitLogRepository.findByHabitId(habitId).stream()
                .filter(log -> log.isCompleted() && !log.getLogDate().isBefore(startOfWeek))
                .count();

        // 8. Build and Return Response DTO
        LogResponseDTO response = new LogResponseDTO();
        response.setHabitLog(savedLog);

        if (weeklyCount >= habit.getTargetDaysPerWeek()) {
            response.setMessage("Weekly Goal Met! 🏆 Continue logging daily to maintain your streak.");
        } else {
            long remaining = habit.getTargetDaysPerWeek() - weeklyCount;
            response.setMessage("Log saved! " + remaining + " more day(s) to reach your weekly goal.");
        }

        return response;
    }

    public List<HabitLog> getLogsByHabit(Long habitId) {
        if (!habitRepository.existsById(habitId)) {
            throw new ResourceNotFoundException("Habit not found with id: " + habitId);
        }
        // Fix: Call habitLogRepository instead of habitRepository [cite: 143]
        return habitLogRepository.findByHabitIdOrderByLogDateDesc(habitId);
    }

    public HabitLog updateLog(Long habitId, LocalDate date, boolean completed) {
        // Requirement: Edit log if needed [cite: 74, 142]
        HabitLog existingLog = habitLogRepository.findByHabitIdAndLogDate(habitId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found for this date"));

        existingLog.setCompleted(completed);
        return habitLogRepository.save(existingLog);
    }


    public StreakResponseDTO calculateStreaks(Long habitId) {
        List<HabitLog> logs = habitLogRepository.findByHabitIdOrderByLogDateDesc(habitId);
        if (logs.isEmpty()) return new StreakResponseDTO(0, 0);

        int longestStreak = 0;
        int tempLongest = 0;
        LocalDate nextExpected = null;

        // LONGEST STREAK: Scans all-time history
        for (HabitLog log : logs) {
            if (log.isCompleted()) {
                if (nextExpected == null || log.getLogDate().equals(nextExpected)) {
                    tempLongest++;
                } else {
                    tempLongest = 1;
                }
                // CRITICAL: Update immediately to catch the 5th day
                longestStreak = Math.max(longestStreak, tempLongest);
                nextExpected = log.getLogDate().minusDays(1);
            } else {
                tempLongest = 0;
                nextExpected = null;
            }
        }

        // CURRENT STREAK: Momentum ending today/yesterday [cite: 81]
        int currentStreak = 0;
        LocalDate today = LocalDate.now();
        HabitLog mostRecent = logs.get(0);

        if (mostRecent.isCompleted() &&
                (mostRecent.getLogDate().equals(today) || mostRecent.getLogDate().equals(today.minusDays(1)))) {
            LocalDate currentExpected = mostRecent.getLogDate();
            for (HabitLog log : logs) {
                if (log.isCompleted() && log.getLogDate().equals(currentExpected)) {
                    currentStreak++;
                    currentExpected = currentExpected.minusDays(1);
                } else {
                    break;
                }
            }
        }
        return new StreakResponseDTO(currentStreak, longestStreak);
    }


}