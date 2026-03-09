package com.litmus7.streakify.controller;

import com.litmus7.streakify.dto.DashboardResponseDTO;
import com.litmus7.streakify.service.HabitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users") // Aggregates data at the user level
public class DashboardController {

    private final HabitService habitService;

    public DashboardController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/{userId}/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboard(@PathVariable Long userId) {
        // This method calculates totalHabits, consistencyScore, and streaks
        DashboardResponseDTO dashboard = habitService.getUserDashboard(userId);
        return ResponseEntity.ok(dashboard);
    }
}