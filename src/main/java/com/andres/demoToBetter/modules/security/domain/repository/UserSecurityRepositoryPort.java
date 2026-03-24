package com.andres.demotobetter.modules.security.domain.repository;

import java.util.Optional;

import com.andres.demotobetter.modules.security.domain.model.UserSecurity;

/**
 * Interface for UserSecurity repository operations.
 * 
 * @author andres
 */
public interface UserSecurityRepositoryPort {
    Optional<UserSecurity> findByEmail(String email);
    UserSecurity save(UserSecurity userSecurity);
    Optional<UserSecurity> findById(Long id);
}

