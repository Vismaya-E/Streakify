package com.litmus7.streakify.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    // Keep ONLY this field. Remove "details".
    private String message;
}