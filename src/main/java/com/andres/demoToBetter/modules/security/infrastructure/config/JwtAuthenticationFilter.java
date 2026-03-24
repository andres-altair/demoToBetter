package com.andres.demotobetter.modules.security.infrastructure.config;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.andres.demotobetter.modules.security.domain.service.TokenServicePort;
import com.andres.demotobetter.modules.security.infrastructure.security.UserDetailsImpl;
import com.andres.demotobetter.modules.security.infrastructure.security.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter class that checks if the request has a valid JWT token and sets the
 * user details in the security context.
 * 
 * @author andres
 */
// modules/security/infrastructure/config/JwtAuthenticationFilter.java

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    // Inyectamos la Interfaz (Port), no la clase concreta (Adapter)
    private final TokenServicePort tokenService; 
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // Usamos el puerto para extraer el username
            String username = tokenService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                // IMPORTANTE: Ajuste de la firma del método
                // Ahora pasamos token y username (String), cumpliendo con el nuevo contrato
                if (userDetails.isEnabled() && tokenService.isTokenValid(token, userDetails.getUsername())) {
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Log context para trazabilidad
                    MDC.put("userId", String.valueOf(((UserDetailsImpl) userDetails).getId()));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired in the request to {}: {}", request.getRequestURI(), e.getMessage());
        } catch (Exception e) {
            log.error("Security filter error: ", e);
        }

        filterChain.doFilter(request, response);
    }
}
