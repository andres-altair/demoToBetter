package com.andres.demotobetter.modules.users.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

import jakarta.validation.Valid;

/**
 * REST controller for managing user profile resources.
 * 
 * @author andres
 */
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
    @GetMapping
    public ResponseEntity<Page<UserProfileDTO>> getAll(
            UserProfileFilterDTO filter,
            Pageable pageable) {
        log.debug("Consultando lista paginada de perfiles con filtros: {}", filter);

        Page<UserProfileDTO> users = userService.findAll(filter, pageable)
                .map(userMapper::toDTO);

        return ResponseEntity.ok(users);
    }

    /**
     * Returns a user profile by ID.
     *
     * @param id the user ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getById(@PathVariable Long id) {
        log.debug("Buscando perfil con ID: {}", id);
        UserProfileDTO user = userMapper.toDTO(userService.findById(id));
        return ResponseEntity.ok(user);
    }

    /**
     * Creates a new user profile.
     *
     * @param dto the data for the new user
     */
    @PostMapping
    public ResponseEntity<UserProfileDTO> create(@Valid @RequestBody UserProfileCreateDTO dto) {
        log.info("Creando nuevo perfil de usuario para: {}", dto.getEmail());

        UserProfileDTO response = userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Deletes a user profile by ID.
     *
     * @param id the user ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Petición para eliminar perfil con ID: {}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing user profile.
     *
     * @param id  the user ID
     * @param dto the updated user data
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> update(@PathVariable Long id, @Valid @RequestBody UserProfileUpdateDTO dto) {
        log.info("Actualizando perfil con ID: {}", id);
        UserProfile user = userMapper.toEntity(dto);
        UserProfile updated = userService.update(id, user);
        UserProfileDTO response = userMapper.toDTO(updated);
        return ResponseEntity.ok(response);
    }
}