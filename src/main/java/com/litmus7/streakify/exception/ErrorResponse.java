package com.litmus7.streakify.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"message","details"})
public class ErrorResponse {
   // private LocalDateTime timestamp;
    private String message;
    private String details;
}