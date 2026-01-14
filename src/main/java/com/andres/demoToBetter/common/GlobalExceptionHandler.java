package com.andres.demoToBetter.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.Valid;
/**
 * Global exception handler for REST controllers.
 * Handles validation errors and runtime exceptions across the entire application.
 * @author andres
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles validation errors triggered by @Valid annotation.
     * Extracts field errors and returns them as a JSON map with HTTP 400 status.
     *
     * @param ex the exception thrown when validation fails
     * @return a ResponseEntity containing the validation errors
     */
    @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handelValidationErrors(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles generic RuntimeExceptions thrown anywhere in the application.
     * Returns a simple error message with HTTP 400 status.
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

}