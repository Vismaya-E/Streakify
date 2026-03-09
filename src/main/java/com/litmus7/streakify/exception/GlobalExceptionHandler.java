package com.litmus7.streakify.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles "Non-existing user" or "Habit not found" [cite: 166]
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); // Returns 404
    }

    // 2. Handles Business Rule violations (Future dates, Duplicate logs, Invalid frequency)
    // You should create a simple 'BadRequestException' class for this
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                " Rule Violation",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); // Returns 400
    }

    // 3. Specific handler for your Email/Unique constraints
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationError(RuntimeException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                "Data Integrity Error",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); // Returns 400
    }

    // 4. Enhanced Validation: Collects ALL field errors for cleaner JSON
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation Failed");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        body.put("details", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); // Returns 400
    }

    // 5. Final Safety Net
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                "Internal Server Error",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR); // Returns 500
    }
}