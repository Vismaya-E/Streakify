package com.litmus7.streakify.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({ "id", "name", "targetDaysPerWeek", "userId", "createdAt" })
public class HabitResponseDTO {

    private Long id;
    private String name;
    private Integer targetDaysPerWeek;
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}