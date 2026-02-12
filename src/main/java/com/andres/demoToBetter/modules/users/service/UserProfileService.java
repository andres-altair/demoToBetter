package com.andres.demotobetter.modules.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.andres.demotobetter.modules.users.dto.UserProfileCreateDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileFilterDTO;
import com.andres.demotobetter.modules.users.entity.UserProfile;

/**
 * Defines the contract for managing user profiles.
 * @author andres
 */
public interface UserProfileService {

    /**
     * Creates a new user profile and its associated security credentials.
     *
     * @param dto data required to create the profile
     * @return the created profile as a DTO
     */
    UserProfileDTO save(UserProfileCreateDTO dto);

    /**
     * Retrieves a paginated list of user profiles based on filter criteria.
     *
     * @param filter filtering options (name, phone, etc.)
     * @param pageable pagination and sorting configuration
     * @return a page of matching user profiles
     */
    Page<UserProfile> findAll(UserProfileFilterDTO filter, Pageable pageable);

    /**
     * Retrieves a user profile by ID.
     *
     * @param id profile identifier
     * @return the user profile
     * @throws com.andres.demotobetter.common.exception.custom.NotFoundException
     *         if the profile does not exist
     */
    UserProfile findById(Long id);

    /**
     * Deletes a user profile by ID.
     *
     * @param id profile identifier
     * @throws com.andres.demotobetter.common.exception.custom.NotFoundException
     *         if the profile does not exist
     */
    void delete(Long id);

    /**
     * Updates an existing user profile.
     *
     * @param id profile identifier
     * @param user new profile data
     * @return the updated profile
     * @throws com.andres.demotobetter.common.exception.custom.NotFoundException
     *         if the profile does not exist
     */
    UserProfile update(Long id, UserProfile user);
}