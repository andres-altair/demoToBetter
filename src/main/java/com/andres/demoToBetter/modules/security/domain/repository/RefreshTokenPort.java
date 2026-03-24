package com.andres.demotobetter.modules.security.domain.repository;

import com.andres.demotobetter.modules.security.domain.model.RefreshToken;
import java.util.Optional;

/**
 * Interface for RefreshToken repository operations.
 * 
 * @author andres
 */
public interface RefreshTokenPort {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
}

