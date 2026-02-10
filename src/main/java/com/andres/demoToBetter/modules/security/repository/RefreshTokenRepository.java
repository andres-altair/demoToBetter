package com.andres.demotobetter.modules.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andres.demotobetter.modules.security.entity.RefreshToken;

/**
 * Repository interface for managing RefreshToken entities.
 * @author andres
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
