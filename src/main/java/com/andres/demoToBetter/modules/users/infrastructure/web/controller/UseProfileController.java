package com.andres.demotobetter.modules.users.infrastructure.web.controller;

import com.andres.demotobetter.modules.users.application.dto.*;
import com.andres.demotobetter.modules.users.application.usecase.*;
import com.andres.demotobetter.modules.users.domain.model.PageQuery;
import com.andres.demotobetter.modules.users.domain.model.PageResponse;
import com.andres.demotobetter.modules.users.domain.model.UserProfileFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user profile resources.
 * Totalmente desacoplado de la lógica de negocio y persistencia.
 */
@Tag(name = "UserProfiles Controller", description = "Endpoints for managing user profiles")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UseProfileController {

    // Inyección de casos de uso individuales (Single Action Use Cases)
    private final CreateUserProfileUseCase createUseCase;
    private final GetUserProfileByIdUseCase getByIdUseCase;
    private final GetUserProfileUseCase getUserUseCase;
    private final UpdateUserProfileUseCase updateUseCase;
    private final DeleteUserProfileUseCase deleteUseCase;

    /**
     * Obtiene una lista paginada de perfiles de usuario.
     */
    @Operation(summary = "List profiles", description = "Provides a paginated list of profiles using Hexagonal SearchPage.")
    @GetMapping
    public ResponseEntity<PageResponse<UserProfileDTO>> getAll(
            @ParameterObject UserProfileFilter filter,
            @ParameterObject Pageable pageable) {

        String sortField = pageable.getSort().stream().findFirst().map(Sort.Order::getProperty).orElse("id");
        String direction = pageable.getSort().stream().findFirst().map(order -> order.getDirection().name())
                .orElse("ASC");

        PageQuery query = new PageQuery(pageable.getPageNumber(), pageable.getPageSize(), sortField, direction);

        return ResponseEntity.ok(getUserUseCase.execute(filter, query));
    }

    /**
     * Obtiene un perfil por su ID.
     */
    @Operation(summary = "Get profile by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getById(@PathVariable Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        return ResponseEntity.ok(getByIdUseCase.execute(id));
    }

    /**
     * Crea un nuevo perfil de usuario.
     */
    @Operation(summary = "Register a new profile")
    @PostMapping
    public ResponseEntity<UserProfileDTO> create(@Valid @RequestBody UserProfileCreateDTO dto) {
        log.info("REST request to save UserProfile : {}", dto.getEmail());

        UserProfileDTO response = createUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un perfil existente.
     */
    @Operation(summary = "Update profile by ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileUpdateDTO dto) {

        log.info("REST request to update UserProfile : {}", id);

        UserProfileDTO response = updateUseCase.execute(id, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina (o desactiva) un perfil por ID.
     */
    @Operation(summary = "Deactivate profile by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("REST request to delete UserProfile : {}", id);

        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
