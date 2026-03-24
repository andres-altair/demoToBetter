package com.andres.demotobetter.modules.security.infrastructure.persistence.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.PermissionEntity;
/**
 * Repository interface for managing Permission entities.
 * @author andres
 */
@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {}