package com.litmus7.streakify.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles "Non-existing user" or "Habit not found"
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        // Returns: { "message": "User not found with id: X" }
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    // 2. Handles Business Rule violations (Future dates, Duplicate logs)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        // Returns: { "message": "Log date cannot be in the future" }
        return new ResponseEntity<>(new ErrorResponse( ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 3. Handles Email/Unique constraints (Data Integrity)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationError(RuntimeException ex) {
        // Returns: { "message": "Email already exists" }
        return new ResponseEntity<>(new ErrorResponse( ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 4. Enhanced Validation: Collects field errors into the 'message' field
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Get the first validation error message (e.g., "Invalid email format")
        String firstError = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        // Returns: { "message": "Invalid email format" }
        return new ResponseEntity<>(new ErrorResponse( firstError), HttpStatus.BAD_REQUEST);
    }

    // 5. Final Safety Net
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Internal Server Error  " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}