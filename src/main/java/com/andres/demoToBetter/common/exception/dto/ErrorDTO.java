package com.andres.demoToBetter.common.exception.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
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
