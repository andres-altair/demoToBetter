package com.andres.demotobetter.modules.users.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.andres.demotobetter.modules.users.infrastructure.persistence.entity.UserProfileEntity;

/**
 * Repository interface for managing UserProfile entities.
 * @author andres
 */
@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, Long>, JpaSpecificationExecutor<UserProfileEntity> { }