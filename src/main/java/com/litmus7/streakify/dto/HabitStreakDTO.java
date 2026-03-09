package com.litmus7.streakify.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "habitName", "currentStreak", "longestStreak" })
public class HabitStreakDTO {
    private String habitName;
    private int currentStreak;
    private int longestStreak;
}