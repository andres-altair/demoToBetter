package com.andres.demotobetter.common.config;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter class that sets the log context for the request.
 * 
 * @author andres
 */
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            org.slf4j.MDC.put("method", request.getMethod());
            org.slf4j.MDC.put("path", request.getRequestURI());
            org.slf4j.MDC.put("ip", request.getRemoteAddr());
            filterChain.doFilter(request, response);
        } finally {
            org.slf4j.MDC.clear();
        }
    }
}