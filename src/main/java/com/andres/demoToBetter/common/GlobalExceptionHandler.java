package com.andres.demoToBetter.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 * @author andres
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles validation errors triggered by @Valid annotation.
     *
     * @param ex the exception thrown when validation fails
     * @return a ResponseEntity containing the validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error-> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles generic RuntimeExceptions thrown anywhere in the application.
     *
     * @param ex the runtime exception thrown
     * @return a ResponseEntity containing the error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,String>> handleRuntime(RuntimeException ex){
        Map<String,String> error= new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle generic Exception thrown anywherein in the application
     * @param ex the exception thrown
     * @return a ReponseEntity containing the error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleUnexpected (Exception ex){
        Map<String,String> error = new HashMap<>();
        error.put("error", "Unexpected error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}