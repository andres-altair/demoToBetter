package com.andres.demotobetter.modules.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andres.demotobetter.modules.security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {}