package com.andres.demotobetter.modules.security.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.RoleEntity;

/**
 * Repository interface for managing Role entities.
 * @author andres
 */
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}