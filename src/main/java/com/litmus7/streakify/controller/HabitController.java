package com.litmus7.streakify.controller;

import com.litmus7.streakify.dto.DashboardResponseDTO;
import com.litmus7.streakify.dto.HabitRequestDTO;
import com.litmus7.streakify.dto.HabitResponseDTO;
import com.litmus7.streakify.entity.Habit;
import com.litmus7.streakify.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping() // Base path for the module
public class HabitController {

    @Autowired
    private HabitService habitService;


    @PostMapping("/habits")
    public ResponseEntity<HabitResponseDTO> createHabit(@Valid @RequestBody HabitRequestDTO request) {
        Habit savedHabit = habitService.saveHabit(request);
        return new ResponseEntity<>(convertToDTO(savedHabit), HttpStatus.CREATED);
    }



    @GetMapping("/users/{userId}/habits")
    public ResponseEntity<List<HabitResponseDTO>> getUserHabits(@PathVariable Long userId) {
        List<Habit> habits = habitService.getHabitsByUserId(userId);

        // Ensure we strictly return the DTO list to hide the User entity
        List<HabitResponseDTO> response = habits.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/habits/{id}")
    public ResponseEntity<Map<String, String>> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Habit deleted successfully");
        return ResponseEntity.ok(response);
    }

    private HabitResponseDTO convertToDTO(Habit habit) {
        HabitResponseDTO dto = new HabitResponseDTO();
        dto.setId(habit.getId());
        dto.setName(habit.getName());
        dto.setTargetDaysPerWeek(habit.getTargetDaysPerWeek());
        dto.setUserId(habit.getUser().getId());
        dto.setCreatedAt(habit.getCreatedAt());
        return dto;
    }
}