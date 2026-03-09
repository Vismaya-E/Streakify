package com.litmus7.streakify.controller;

import com.litmus7.streakify.dto.HabitLogRequestDTO;
import com.litmus7.streakify.dto.LogResponseDTO;
import com.litmus7.streakify.dto.StreakResponseDTO;
import com.litmus7.streakify.entity.HabitLog;
import com.litmus7.streakify.exception.ResourceNotFoundException;
import com.litmus7.streakify.repository.HabitRepository;
import com.litmus7.streakify.service.HabitLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/habits") // Base path starts with habits
public class HabitLogController {

    @Autowired
    private HabitLogService habitLogService;
    @Autowired
    private HabitRepository habitRepository;


    private LogResponseDTO convertToLogDTO(HabitLog log) {
        LogResponseDTO response = new LogResponseDTO();
        response.setHabitLog(log);
        response.setMessage("Log process successfully");
        return response;
    }

    @PostMapping("/{habitId}/logs")
    public ResponseEntity<LogResponseDTO> logHabit(
            @PathVariable Long habitId,
            @Valid @RequestBody HabitLogRequestDTO logRequest) {

        LogResponseDTO response = habitLogService.logHabit( habitId, logRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{habitId}/logs")
    public ResponseEntity<List<LogResponseDTO>> getLogsByHabit(@PathVariable Long habitId) {
        List<HabitLog> logs = habitLogService.getLogsByHabit(habitId);
        List<LogResponseDTO> response = logs.stream()
                .map(this::convertToLogDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{habitId}/logs/{date}")
    public ResponseEntity<LogResponseDTO> updateLog(
            @PathVariable Long habitId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Valid @RequestBody HabitLogRequestDTO logRequest) {

        HabitLog updatedLog = habitLogService.updateLog(habitId, date, logRequest.isCompleted());
        return ResponseEntity.ok(convertToLogDTO(updatedLog));
    }

    @GetMapping("/{habitId}/streak")
    public ResponseEntity<StreakResponseDTO> getStreak(@PathVariable Long habitId){
        if(!habitRepository.existsById(habitId)){
            throw new ResourceNotFoundException("Habit not found with id: +"+habitId);
        }

        StreakResponseDTO streak=habitLogService.calculateStreaks(habitId);
        return ResponseEntity.ok(streak);
    }

}