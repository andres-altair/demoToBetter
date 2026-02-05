package com.andres.demotobetter.modules.users.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.common.exception.custom.NotFoundException;
import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.service.UserSecurityService;
import com.andres.demotobetter.modules.users.dto.UserProfileCreateDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileFilterDTO;
import com.andres.demotobetter.modules.users.entity.UserProfile;
import com.andres.demotobetter.modules.users.mapper.UserProfileMapper;
import com.andres.demotobetter.modules.users.repository.UserProfileRepository;
import com.andres.demotobetter.modules.users.spec.UserProfileSpecification;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Service layer responsible for handling business logic related to User.
 * 
 * @author andres
 */
@Service
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private static final int MAX_PAGE_SIZE = 50;
    // Allowed fields for sorting (must match UserProfile properties)
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "firstName", "lastName", "phone");
    private static final String ERR_BAD_REQUEST = "USR_400";
    private static final String ERR_NOT_FOUND = "USR_404";

    private final UserProfileRepository repository;
    private final UserSecurityService  userSecurityService;
    private final UserProfileMapper mapper;

    /**
     * Returns a paginated list of user profiles applying dynamic filters.
     */
    @Override
    public Page<UserProfile> findAll(UserProfileFilterDTO filter, Pageable pageable) {

        if (pageable.getPageSize() > 50) {
            throw new BadRequestException(ERR_BAD_REQUEST, "Page size cannot exceed " + MAX_PAGE_SIZE);
        }

        if (pageable.getPageNumber() < 0) {
            throw new BadRequestException(ERR_BAD_REQUEST, "Page number cannot be negative");
        }

        pageable.getSort().forEach(order -> {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new BadRequestException(ERR_BAD_REQUEST,
                        "Sorting by '" + order.getProperty() + "' is not allowed");
            }
        });

        Specification<UserProfile> spec = Specification.allOf(
                UserProfileSpecification.firstNameContains(filter.getFirstName()),
                UserProfileSpecification.lastNameContains(filter.getLastName()),
                UserProfileSpecification.phoneContains(filter.getPhone()));

        return repository.findAll(spec, pageable);
    }

    /**
     * Retrieves a user profile by ID or throws an exception if not found.
     */
    @Override
    @SuppressWarnings("null")
    public UserProfile findById(Long id) {
        validateId(id);

        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(ERR_NOT_FOUND, "User with ID " + id + " does not exist"));
    }

    /**
     * Creates or updates a user profile.
     */

    @Transactional
    @Override
    public UserProfileDTO save(@NonNull UserProfileCreateDTO dto) {
        UserSecurity security = userSecurityService.createSecurityUser(
            dto.getEmail(), 
            dto.getPassword(),
            dto.getRoles());
        UserProfile profile = mapper.toEntity(dto);
        profile.setUserSecurity(security);
        repository.save(profile);
        return mapper.toDTO(profile);
    }

    /**
     * Deletes a user profile by ID if it exists.
     */
    @Override
    @SuppressWarnings("null")
    public void delete(Long id) {
        validateId(id);

        if (!repository.existsById(id)) {
            throw new NotFoundException(ERR_NOT_FOUND, "User with ID " + id + " does not exist");
        }
        repository.deleteById(id);
    }

    /**
     * Updates editable fields of an existing user profile.
     */
    @Override
    public UserProfile update(Long id, UserProfile updatedUser) {
        validateId(id);

        @SuppressWarnings("null")
        UserProfile existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ERR_NOT_FOUND, "User with ID " + id + " does not exist"));

        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setPhone(updatedUser.getPhone());
        existing.setAvatarUrl(updatedUser.getAvatarUrl());
        return repository.save(existing);
    }

    /**
     * Ensures the provided ID is not null.
     */
    private void validateId(Long id) {
        if (id == null) {
            throw new BadRequestException(ERR_BAD_REQUEST, "ID cannot be null");
        }
    }
}