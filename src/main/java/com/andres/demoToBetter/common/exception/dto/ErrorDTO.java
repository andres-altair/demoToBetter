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

    @Schema(description = "Código interno del error", example = "AUTH_401")
    private String code;

    @Schema(description = "Mensaje principal del error", example = "Authentication failed")
    private String message;

    @Schema(description = "Detalle adicional del error", example = "Invalid credentials")
    private String detail;

    @Schema(description = "Código de estado HTTP", example = "401")
    private int status;

    @Schema(description = "Ruta donde ocurrió el error", example = "/api/auth/login")
    private String path;

    @Schema(description = "Identificador único para trazabilidad", example = "c1a2b3d4-e5f6-7890-abcd-1234567890ef")
    private String traceId;

    @Schema(description = "Fecha y hora del error", example = "2025-02-17T10:15:30")
    private LocalDateTime timestamp;
}