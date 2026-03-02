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
import lombok.extern.slf4j.Slf4j;

/**
 * Class that implements UserProfileService.
 * 
 * @author andres
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private static final int MAX_PAGE_SIZE = 50;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "firstName", "lastName", "phone");
    private static final String ERR_BAD_REQUEST = "USR_400";
    private static final String ERR_NOT_FOUND = "USR_404";

    private final UserProfileRepository repository;
    private final UserSecurityService userSecurityService;
    private final UserProfileMapper mapper;

    @Override
    public Page<UserProfile> findAll(UserProfileFilterDTO filter, Pageable pageable) {
        log.debug("Running paginated user search. Filters: {}", filter);
        if (pageable.getPageSize() > 50) {
            log.warn("Request for excessive page size: {}", pageable.getPageSize());
            throw new BadRequestException(ERR_BAD_REQUEST, "Page size cannot exceed " + MAX_PAGE_SIZE);
        }

        if (pageable.getPageNumber() < 0) {
            log.warn("Attempt to sort by field not allowed: {}", pageable.getPageNumber());
            throw new BadRequestException(ERR_BAD_REQUEST, "Page number cannot be negative");
        }

        pageable.getSort().forEach(order -> {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                log.warn("Attempt to sort by field not allowed: {}", order.getProperty());
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

    @Override
    @SuppressWarnings("null")
    public UserProfile findById(Long id) {
        log.debug("Looking for user profile with ID: {}", id);
        validateId(id);

        return repository.findById(id).orElseThrow(() -> {
            log.warn("Search failed: User with ID {} does not exist", id);
            return new NotFoundException(ERR_NOT_FOUND, "User with ID " + id + " does not exist");
        });
    }

    /**
     * This method is transactional because it touches both the security
     * and user profile tables.
     */
    @Transactional
    @Override
    public UserProfileDTO save(UserProfileCreateDTO dto) {
        log.info("Creating new user: {}", dto.getEmail());
        UserSecurity security = userSecurityService.createSecurityUser(
                dto.getEmail(),
                dto.getPassword(),
                dto.getRoles());
        UserProfile profile = mapper.toEntity(dto);
        profile.setUserSecurity(security);
        repository.save(profile);
        log.info("User saved successfully with ID: {}", profile.getUserSecurity().getId());
        return mapper.toDTO(profile);
    }

    @Override
    public void delete(Long id) {
        log.info("Deactivating user with ID: {}", id);
        validateId(id);

        userSecurityService.disableUser(id);
        log.info("User ID: {} marked as inactive", id);
    }

    @Override
    public UserProfile update(Long id, UserProfile updatedUser) {
        log.info("Starting profile update ID: {}", id);

        validateId(id);

        @SuppressWarnings("null")
        UserProfile existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed: User with ID {} does not exist", id);
                    return new NotFoundException(ERR_NOT_FOUND, "User with ID " + id + " does not exist");
                });

        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setPhone(updatedUser.getPhone());
        existing.setAvatarUrl(updatedUser.getAvatarUrl());
        log.info("Profile ID: {} successfully updated", id);
        return repository.save(existing);
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new BadRequestException(ERR_BAD_REQUEST, "ID cannot be null");
        }
    }
}