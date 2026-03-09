package com.andres.demotobetter.modules.security.domain.model;

import java.util.Date;

/**
 * Record de dominio para el Refresh Token (Java Puro).
 */
public record RefreshToken(
    Long id,
    String token,
    Date expiryDate,
    boolean revoked,
    UserSecurity user // Relación con el modelo de dominio UserSecurity
) {
    // Método de negocio para saber si ha expirado sin depender de JPA
    public boolean isExpired() {
        return expiryDate.before(new Date());
    }
}
