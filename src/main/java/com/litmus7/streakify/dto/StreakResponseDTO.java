package com.litmus7.streakify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StreakResponseDTO {
    private int currentStreak;
    private int longestStreak;

}
