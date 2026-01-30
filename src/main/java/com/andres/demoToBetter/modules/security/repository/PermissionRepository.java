package com.andres.demotobetter.modules.security.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.andres.demotobetter.modules.security.model.Permission;


public interface PermissionRepository extends JpaRepository<Permission, Long> {}