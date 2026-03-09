package com.andres.demotobetter.modules.security.domain.repository;

import java.util.Optional;

import com.andres.demotobetter.modules.security.domain.model.Role;

public interface RoleRepositoryPort {
    Optional<Role> findByName(String name);
}

