package com.andres.demotobetter.modules.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andres.demotobetter.modules.security.model.UserSecurity;

public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {}
