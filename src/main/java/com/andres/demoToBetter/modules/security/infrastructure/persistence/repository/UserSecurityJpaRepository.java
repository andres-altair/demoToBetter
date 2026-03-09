package com.andres.demotobetter.modules.security.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.UserSecurityEntity;

/**
 * Repository interface for managing UserSecurity entities.
 * @author andres
 */
public interface UserSecurityJpaRepository extends JpaRepository<UserSecurityEntity, Long> {

    Optional<UserSecurityEntity> findByEmail(String email);
}
