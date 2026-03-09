package com.andres.demotobetter.common.infrastructure.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.andres.demotobetter.common.domain.*;
import com.andres.demotobetter.common.infrastructure.web.dto.ErrorDTO;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Class used to manage global handle exceptions.
 * 
 * @author andres
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(
                "ARG_400",
                message,
                "Validation failed for the request parameters",
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(BaseBusinessException.class)
    public ResponseEntity<ErrorDTO> handleApiExceptions(BaseBusinessException ex, HttpServletRequest request) {

        HttpStatus status = switch (ex) {
            case NotFoundException e -> HttpStatus.NOT_FOUND;
            case BadRequestException e -> HttpStatus.BAD_REQUEST;
            case ConflictException e -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };

        String detail = switch (ex) {
            case NotFoundException e -> "The requested resource was not found";
            case BadRequestException e -> "The request could not be processed due to invalid data";
            case ConflictException e -> "There is a conflict with the current state of the resource";
            default -> "An error occurred while processing the business logic";
        };

        return buildResponse(
                ex.getCode(),
                ex.getMessage(),
                detail,
                status,
                request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("CRITICAL ERROR - Unexpected exception at {}: ", request.getRequestURI(), ex);

        return buildResponse(
                "SYS_500",
                "Internal server error",
                "An unexpected error occurred. Please contact support with the traceId.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("Route not found [404]: {}", request.getRequestURI());

        return buildResponse(
                "API_404",
                "The requested URL does not exist.",
                "Verify the endpoint: " + request.getRequestURI(),
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDTO> handleAuth(AuthenticationException ex, HttpServletRequest request) {
        return buildResponse(
                "AUTH_401",
                "Authentication failed",
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(
                "AUTH_403",
                "Access denied",
                "You do not have the required permissions to access this resource",
                HttpStatus.FORBIDDEN,
                request);
    }

    /**
     * Builds a standardized ErrorDTO response and logs the event.
     */
    private ResponseEntity<ErrorDTO> buildResponse(
            String code,
            String message,
            String detail,
            HttpStatus status,
            HttpServletRequest request) {

        String currentTraceId = MDC.get("traceId");
        if (currentTraceId == null || currentTraceId.isEmpty()) {
            currentTraceId = UUID.randomUUID().toString();
        }

        if (status.is4xxClientError()) {
            log.warn("Client Error [{}] at {}: {}", code, request.getRequestURI(), message);
        } else if (status.is5xxServerError() && !code.equals("SYS_500")) {
            log.error("Server Error [{}] at {}: {}", code, request.getRequestURI(), message);
        }

        ErrorDTO error = ErrorDTO.builder()
                .code(code)
                .message(message)
                .detail(detail)
                .status(status.value())
                .path(request.getRequestURI())
                .traceId(currentTraceId)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }
}