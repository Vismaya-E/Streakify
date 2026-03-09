package com.litmus7.streakify.service;

import com.litmus7.streakify.dto.DashboardResponseDTO;
import com.litmus7.streakify.dto.HabitRequestDTO;
import com.litmus7.streakify.dto.HabitStreakDTO;
import com.litmus7.streakify.dto.StreakResponseDTO;
import com.litmus7.streakify.entity.Habit;
import com.litmus7.streakify.entity.HabitLog;
import com.litmus7.streakify.entity.User;
import com.litmus7.streakify.exception.ResourceNotFoundException;
import com.litmus7.streakify.repository.HabitLogRepository;
import com.litmus7.streakify.repository.HabitRepository;
import com.litmus7.streakify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HabitService {
    @Autowired
    public HabitRepository habitRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public HabitLogService habitLogService;

    @Autowired
    public HabitLogRepository habitLogRepository;

    public Habit saveHabit(HabitRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Cannot create habit .User not found."));

        Habit habit = new Habit();
        habit.setName(dto.getName());
        habit.setTargetDaysPerWeek(dto.getTargetDaysPerWeek());
        habit.setUser(user);

        return habitRepository.save(habit);
    }

    public List<Habit> getHabitsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return habitRepository.findByUserId(userId);

    }

    public void deleteHabit(Long habitId) {
        if (!habitRepository.existsById(habitId)) {
            throw new ResourceNotFoundException("Habit not found with id: " + habitId);
        }
        habitRepository.deleteById(habitId);
    }

    public DashboardResponseDTO getUserDashboard(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<Habit> habits = habitRepository.findByUserId(userId);
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));

        int totalHabits = habits.size();
        int activeHabits = 0;
        int completedTodayCount = 0;
        long totalActualCompletions = 0;
        long totalPossibleCompletions = 0;

        List<HabitStreakDTO> streakList = new ArrayList<>();

        for (Habit habit : habits) {
            // 1. Calculate Daily Streaks
            StreakResponseDTO streaks = habitLogService.calculateStreaks(habit.getId());
            HabitStreakDTO streakDto = new HabitStreakDTO();
            streakDto.setHabitName(habit.getName());
            streakDto.setCurrentStreak(streaks.getCurrentStreak());
            streakDto.setLongestStreak(streaks.getLongestStreak());
            streakList.add(streakDto);

            // Fetch logs once for this habit
            List<HabitLog> allLogs = habitLogRepository.findByHabitId(habit.getId());

            // 2. Active Habit Check (Any 'true' in last 7 days)
            boolean hasRecentActivity = allLogs.stream()
                    .filter(log -> !log.getLogDate().isBefore(startOfWeek))
                    .anyMatch(HabitLog::isCompleted);
            if (hasRecentActivity) activeHabits++;

            // 3. Today's Completion Status
            boolean loggedToday = habitLogRepository.findByHabitIdAndLogDate(habit.getId(), today)
                    .map(HabitLog::isCompleted).orElse(false);
            if (loggedToday) completedTodayCount++;

            // 4.filter from sunday
            long weeklyTarget = habit.getTargetDaysPerWeek();
            long actualSuccessesThisWeek = allLogs.stream()
                    .filter(log -> log.isCompleted() && !log.getLogDate().isBefore(startOfWeek))
                    .count();

            // Aggregate totals with the 100% cap logic
            totalActualCompletions += Math.min(actualSuccessesThisWeek, weeklyTarget);
            totalPossibleCompletions += weeklyTarget;
        }

        // Final Percentage Calculation
        int overallConsistency = (totalPossibleCompletions > 0)
                ? (int) Math.round(((double) totalActualCompletions / totalPossibleCompletions) * 100)
                : 0;

        DashboardResponseDTO dashboard = new DashboardResponseDTO();
        dashboard.setTotalHabits(totalHabits);
        dashboard.setActiveHabits(activeHabits);
        dashboard.setCompletedToday(completedTodayCount);
        dashboard.setCurrentStreaks(streakList);
        dashboard.setConsistencyScore(overallConsistency);

        return dashboard;
    }

}
