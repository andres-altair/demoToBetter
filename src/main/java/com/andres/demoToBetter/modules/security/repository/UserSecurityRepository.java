package com.andres.demotobetter.modules.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andres.demotobetter.modules.security.entity.UserSecurity;

/**
 * Repository interface for managing UserSecurity entities.
 * @author andres
 */
public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {

    Optional<UserSecurity> findByEmail(String email);
}
