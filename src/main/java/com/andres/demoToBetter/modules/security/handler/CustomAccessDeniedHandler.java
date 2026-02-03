package com.andres.demotobetter.modules.security.handler;

import com.andres.demotobetter.common.exception.dto.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex) throws IOException {

        ErrorDTO error = ErrorDTO.builder()
                .code("AUTH_403")
                .message("Access denied")
                .detail(ex.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write( new ObjectMapper().writeValueAsString(error));
    }
}
