package com.andres.demotobetter.common.exception.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a standardized error response.
 * 
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {

    @Schema(description = "Internal error code", example = "AUTH_401")
    private String code;

    @Schema(description = "Main error message", example = "Authentication failed")
    private String message;

    @Schema(description = "Additional details of the error", example = "Invalid credentials")
    private String detail;

    @Schema(description = "HTTP status code", example = "401")
    private int status;

    @Schema(description = "Route where the error occurred", example = "/api/auth/login")
    private String path;

    @Schema(description = "Unique identifier for traceability", example = "c1a2b3d4-e5f6-7890-abcd-1234567890ef")
    private String traceId;

    @Schema(description = "Date and time of the error", example = "2025-02-17T10:15:30")
    private LocalDateTime timestamp;
}