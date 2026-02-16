package com.andres.demotobetter.modules.security.config;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.andres.demotobetter.modules.security.model.UserDetailsImpl;
import com.andres.demotobetter.modules.security.service.UserDetailsServiceImpl;
import com.andres.demotobetter.modules.security.service.jwt.JwtService;

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
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                if (!userDetails.isEnabled()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    // Llenamos el userId para nuestro log JSON empresarial
                    MDC.put("userId", String.valueOf(((UserDetailsImpl) userDetails).getId()));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Logueamos la advertencia con el traceId que ya puso el LogContextFilter
            log.warn("JWT expirado en la petición a {}: {}", request.getRequestURI(), e.getMessage());
            // No hacemos nada más. Al no setear la autenticación, 
            // Spring Security lanzará automáticamente el CustomAuthenticationEntryPoint.
        } catch (io.jsonwebtoken.JwtException e) {
            log.error("Token JWT inválido o corrupto: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado en el filtro de seguridad: ", e);
        }

        filterChain.doFilter(request, response);
    }
}