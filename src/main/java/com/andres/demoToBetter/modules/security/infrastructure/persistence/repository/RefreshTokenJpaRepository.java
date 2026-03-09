package com.andres.demotobetter.modules.security.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.RefreshTokenEntity;

/**
 * Repository interface for managing RefreshToken entities.
 * @author andres
 */
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
}
