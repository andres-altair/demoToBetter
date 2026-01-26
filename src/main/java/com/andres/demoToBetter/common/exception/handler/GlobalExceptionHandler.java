package com.andres.demotobetter.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.andres.demotobetter.common.exception.custom.*;
import com.andres.demotobetter.common.exception.dto.ErrorDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return buildResponse(
                "ARG_400",
                message,
                "Invalid application",
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDTO> handleApiExceptions(ApiException ex, HttpServletRequest request) {

        HttpStatus status = switch (ex) {
            case NotFoundException e   -> HttpStatus.NOT_FOUND;
            case BadRequestException e -> HttpStatus.BAD_REQUEST;
            case ConflictException e   -> HttpStatus.CONFLICT;
            default                    -> HttpStatus.BAD_REQUEST;
        };

        String detail = switch (ex) {
            case NotFoundException e   -> "Resource not found";
            case BadRequestException e -> "Invalid application";
            case ConflictException e   -> "Conflict in the operation";
            default                    -> "Business mistake";
        };

        return buildResponse(
                ex.getCode(),
                ex.getMessage(),
                detail,
                status,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleUnexpected(Exception ex, HttpServletRequest request) {
        return buildResponse(
                "SYS_500",
                "Internal error",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<ErrorDTO> buildResponse(
            String code,
            String message,
            String detail,
            HttpStatus status,
            HttpServletRequest request
    ) {
        ErrorDTO error = ErrorDTO.builder()
                .code(code)
                .message(message)
                .detail(detail)
                .status(status.value())
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }
}
