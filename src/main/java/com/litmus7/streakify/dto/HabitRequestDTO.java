package com.litmus7.streakify.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HabitRequestDTO {

    @NotBlank(message = "Habit name cannot be empty")
    private String name;

    @NotNull(message = "Target days are required")
    @Min(value = 1, message = "Target must be at least 1 day")
    @Max(value = 7, message = "Target cannot exceed 7 days")
    private Integer targetDaysPerWeek;

    @NotNull(message = "User ID is required to link the habit")
    private Long userId;
}