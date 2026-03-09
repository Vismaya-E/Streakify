package com.litmus7.streakify.dto;

import com.litmus7.streakify.entity.HabitLog;
import lombok.Data;

@Data
public class LogResponseDTO {
    private HabitLog habitLog;
    private String message;
}