package com.andres.demotobetter.modules.security.domain.repository;

import java.util.Optional;

import com.andres.demotobetter.modules.security.domain.model.Role;

/**
 * Interface for Role repository operations.
 * 
 * @author andres
 */
public interface RoleRepositoryPort {
    Optional<Role> findByName(String name);
}

