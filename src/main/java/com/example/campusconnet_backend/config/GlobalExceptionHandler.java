package com.example.campusconnet_backend.config;

import com.example.campusconnet_backend.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        // Check for specific error messages to return appropriate status codes
        if (message != null) {
            if (message.contains("not found")) {
                status = HttpStatus.NOT_FOUND;
            } else if (message.contains("already exists") || message.contains("already registered")) {
                status = HttpStatus.CONFLICT; // 409
            } else if (message.contains("required") || message.contains("Invalid")) {
                status = HttpStatus.BAD_REQUEST; // 400
            }
        }
        
        ErrorResponse error = new ErrorResponse(message != null ? message : "An error occurred");
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Registration already exists for this user and event";
        
        // Check both exception message and root cause
        String errorMsg = ex.getMessage() != null ? ex.getMessage() : "";
        Throwable rootCause = ex.getRootCause();
        String rootCauseMsg = rootCause != null ? rootCause.getMessage() : "";
        
        // Check if it's a unique constraint violation for registrations
        if (errorMsg.contains("uk_registrations_user_event") || 
            errorMsg.contains("Duplicate entry") ||
            errorMsg.contains("unique constraint") ||
            rootCauseMsg.contains("uk_registrations_user_event") ||
            rootCauseMsg.contains("Duplicate entry") ||
            rootCauseMsg.contains("unique constraint")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(message));
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Data integrity violation: " + errorMsg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
