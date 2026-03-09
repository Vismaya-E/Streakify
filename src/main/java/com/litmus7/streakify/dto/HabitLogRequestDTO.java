package com.litmus7.streakify.dto;
import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

@Data
public class HabitLogRequestDTO {

    private LocalDate logDate;
    @NotNull(message="Completion Status is required")
    private boolean completed;
}