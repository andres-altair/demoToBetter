package com.andres.demotobetter.modules.security.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andres.demotobetter.modules.security.entity.Permission;
/**
 * Repository interface for managing Permission entities.
 * @author andres
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {}