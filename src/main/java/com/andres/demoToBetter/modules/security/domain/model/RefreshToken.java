package com.andres.demotobetter.modules.security.domain.model;

import java.util.Date;

/**
 * Class representing a refresh token in the system.
 * 
 * @author andres
 */
public record RefreshToken(
    Long id,
    String token,
    Date expiryDate,
    boolean revoked,
    UserSecurity user 
) {
    
    public boolean isExpired() {
        return expiryDate.before(new Date());
    }
}
