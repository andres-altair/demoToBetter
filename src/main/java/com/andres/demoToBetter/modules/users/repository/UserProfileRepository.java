package com.andres.demotobetter.modules.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.andres.demotobetter.modules.users.model.UserProfile;


/**
 * Repository interface for managing UserProfile entities.
 * @author andres
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> { }