package com.andres.demotobetter.modules.users.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.andres.demotobetter.modules.users.dto.UserProfileCreateDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileFilterDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileUpdateDTO;
import com.andres.demotobetter.modules.users.entity.UserProfile;
import com.andres.demotobetter.modules.users.mapper.UserProfileMapper;
import com.andres.demotobetter.modules.users.service.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing user profile resources.
 * 
 * @author andres
 */
@Tag(name = "UserProfiles Controller", description = "Endpoints for managing user profiles")
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UseProfileController {

    private final UserProfileService userService;
    private final UserProfileMapper userMapper;

    /**
     * Returns a paginated list of user profiles using optional filter criteria.
     *
     * @param filter   object containing optional filtering fields (firstName,
     *                 lastName, phone)
     * @param pageable pagination and sorting information
     */
    @Operation(summary = "List profiles", description = "It provides a paginated list of active profiles.", parameters = {
            @Parameter(name = "firstName", description = "Filter by first name", example = "User"),
            @Parameter(name = "lastName", description = "Filter by last name", example = "Last"),
            @Parameter(name = "phone", description = "Filter by phone", example = "600001"),
            @Parameter(name = "page", description = "Page number", example = "0"),
            @Parameter(name = "size", description = "Page size", example = "10"),
            @Parameter(name = "sort", description = "Sorting field and direction", example = "firstName,asc")
    })
    @GetMapping
    public ResponseEntity<Page<UserProfileDTO>> getAll(
            @ParameterObject UserProfileFilterDTO filter, @ParameterObject Pageable pageable) {
        log.debug("Viewing paginated list of profiles with filters: {}", filter);

        Page<UserProfileDTO> users = userService.findAll(filter, pageable)
                .map(userMapper::toDTO);

        return ResponseEntity.ok(users);
    }

    /**
     * Returns a user profile by ID.
     *
     * @param id the user ID
     */
    @Operation(summary = "Get profile by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getById(@PathVariable Long id) {
        log.debug("Looking for profile with ID: {}", id);
        UserProfileDTO user = userMapper.toDTO(userService.findById(id));
        return ResponseEntity.ok(user);
    }

    /**
     * Creates a new user profile.
     *
     * @param dto the data for the new user
     */
    @Operation(summary = "Register a new profile")
    @PostMapping
    public ResponseEntity<UserProfileDTO> create(@Valid @RequestBody UserProfileCreateDTO dto) {
        log.info("Crating new profile for: {}", dto.getEmail());

        UserProfileDTO response = userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Deletes a user profile by ID.
     *
     * @param id the user ID
     */
    @Operation(summary = "Deactivate profile by ID", description = "Deactivate the profile with the provided ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request to delete profile with ID: {}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing user profile.
     *
     * @param id  the user ID
     * @param dto the updated user data
     */
    @Operation(summary = "Update profile by ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> update(@PathVariable Long id, @Valid @RequestBody UserProfileUpdateDTO dto) {
        log.info("Updating profile with ID: {}", id);
        UserProfile user = userMapper.toEntity(dto);
        UserProfile updated = userService.update(id, user);
        UserProfileDTO response = userMapper.toDTO(updated);
        return ResponseEntity.ok(response);
    }
}