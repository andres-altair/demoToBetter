package com.andres.demotobetter.common.exception.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object representing a standardized error response.
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {
    private String code;        
    private String message;       
    private String detail;       
    private int status;          
    private String path;          
    private String traceId;
    private LocalDateTime timestamp;
}
