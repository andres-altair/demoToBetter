package com.andres.demotobetter.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.andres.demotobetter.common.exception.custom.*;
import com.andres.demotobetter.common.exception.dto.ErrorDTO;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Class responsible for converting application exceptions into standardized API error responses.
 * 
 * @author andres
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(ex.getCode(), ex.getMessage(), "Resource not found", 404, request));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(ex.getCode(), ex.getMessage(), "Invalid application", 400, request));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDTO> handleConflict(ConflictException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(ex.getCode(), ex.getMessage(), "Conflict in the operation", 409, request));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDTO> handleBusiness(ApiException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(ex.getCode(), ex.getMessage(), "Business mistake", 400, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleUnexpected(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("SYS_500", "Internal error", ex.getMessage(), 500, request));
    }

    /**
     * Utility method for building a standardized ErrorDTO object.
     *
     * @param code    custom application error code
     * @param message human-readable error message
     * @param detail  additional context about the error
     * @param status  HTTP status code
     * @param request the HTTP request that triggered the error
     */
    private ErrorDTO buildError(String code, String message, String detail, int status, HttpServletRequest request) {
        return ErrorDTO.builder()
                .code(code)
                .message(message)
                .detail(detail)
                .status(status)
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
