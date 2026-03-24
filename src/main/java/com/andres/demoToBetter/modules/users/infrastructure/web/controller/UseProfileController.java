package com.andres.demotobetter.modules.users.infrastructure.web.controller;

import com.andres.demotobetter.modules.users.application.dto.*;
import com.andres.demotobetter.modules.users.application.usecase.*;
import com.andres.demotobetter.modules.users.domain.model.PageQuery;
import com.andres.demotobetter.modules.users.domain.model.PageResponse;
import com.andres.demotobetter.modules.users.domain.model.UserProfileFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * 
 * @author andres
 */
@Tag(name = "UserProfiles Controller", description = "Endpoints for managing user profiles")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UseProfileController {

    private final CreateUserProfileUseCase createUseCase;
    private final GetUserProfileByIdUseCase getByIdUseCase;
    private final GetUserProfileUseCase getUserUseCase;
    private final UpdateUserProfileUseCase updateUseCase;
    private final DeleteUserProfileUseCase deleteUseCase;

    @Operation(summary = "List profiles", description = "It provides a paginated list of active profiles.", parameters = {
            @Parameter(name = "firstName", description = "Filter by first name", example = "User"),
            @Parameter(name = "lastName", description = "Filter by last name", example = "Last"),
            @Parameter(name = "phone", description = "Filter by phone", example = "600001"),
            @Parameter(name = "page", description = "Page number", example = "0"),
            @Parameter(name = "size", description = "Page size", example = "10"),
            @Parameter(name = "sort", description = "Sorting field and direction", array = @ArraySchema(schema = @Schema(type = "string", example = "firstName,asc")))
    })

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

    @Operation(summary = "Get profile by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getById(@PathVariable Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        return ResponseEntity.ok(getByIdUseCase.execute(id));
    }

    @Operation(summary = "Register a new profile")
    @PostMapping
    public ResponseEntity<UserProfileDTO> create(@Valid @RequestBody UserProfileCreateDTO dto) {
        log.info("REST request to save UserProfile : {}", dto.getEmail());

        UserProfileDTO response = createUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Update profile by ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileUpdateDTO dto) {

        log.info("REST request to update UserProfile : {}", id);

        UserProfileDTO response = updateUseCase.execute(id, dto);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Deactivate profile by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("REST request to delete UserProfile : {}", id);

        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
