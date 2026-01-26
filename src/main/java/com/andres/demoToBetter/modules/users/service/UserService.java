package com.andres.demotobetter.modules.users.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.common.exception.custom.ConflictException;
import com.andres.demotobetter.common.exception.custom.NotFoundException;
import com.andres.demotobetter.modules.users.dto.UserFilterDTO;
import com.andres.demotobetter.modules.users.model.User;
import com.andres.demotobetter.modules.users.repository.UserRepository;
import com.andres.demotobetter.modules.users.spec.UserSpecification;

import lombok.AllArgsConstructor;

/**
 * Service layer responsible for handling business logic related to User.
 * @author andres
 */
@Service
@AllArgsConstructor
public class UserService { 
    private static final int MAX_PAGE_SIZE = 50;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "username", "email");
    private static final String ERR_BAD_REQUEST = "USR_400";
    private static final String ERR_CONFLICT = "USR_409";
    private static final String ERR_NOT_FOUND = "USR_404";

    private final UserRepository repository; 

    /**
     * Retrieves a paginated list of users applying optional filtering and sorting rules.
     *
     * @param filter   DTO containing optional filter values (username, email)
     * @param pageable pagination and sorting information provided by Spring
     * @return a paginated list of User entities matching the filter criteria
     */
    public Page<User> findAll(UserFilterDTO filter, Pageable pageable) {

        if (pageable.getPageSize() > 50) {
            throw new BadRequestException(ERR_BAD_REQUEST,"Page size cannot exceed " + MAX_PAGE_SIZE);
        }

        if (pageable.getPageNumber() < 0) {
            throw new BadRequestException(ERR_BAD_REQUEST,"Page number cannot be negative");
        }

        pageable.getSort().forEach(order -> {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new BadRequestException(ERR_BAD_REQUEST,"Sorting by '" + order.getProperty() + "' is not allowed");
            }
        });

        Specification<User> spec = Specification.allOf(
        UserSpecification.usernameContains(filter.getUsername()),
        UserSpecification.emailContains(filter.getEmail()));
        
        return repository.findAll(spec,pageable);
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user
     * @return an Optional containing the User if found, otherwise empty
     */
    @SuppressWarnings("null")
    public User findById(Long id) { 
        validateId(id);

        return repository.findById(id).orElseThrow(
            () -> new NotFoundException( ERR_NOT_FOUND, "User with ID " + id + " does not exist" ));
    } 

    /**
     * Retrieves a user by its username.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, otherwise empty
     */
    public Optional<User> findByUsername(String username) { 
        if (username == null) {
            throw new BadRequestException(ERR_BAD_REQUEST, "Username cannot be null");
        }
        return repository.findByUsername(username);
    } 

    /**
     * Saves a new user or updates an existing one.
     *
     * @param user the User entity to save
     * @return the saved User entity
     */
    public User save(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new ConflictException( ERR_CONFLICT, "Email already exists");
        }
        return repository.save(user);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete
     */
    @SuppressWarnings("null")
    public void delete(Long id) { 
        validateId(id);

        if (!repository.existsById(id)) {
            throw new NotFoundException( ERR_NOT_FOUND, "User with ID " + id + " does not exist" );
        }
        repository.deleteById(id); 
    } 

    /**
     * Updates an existing user.
     * 
     * @param id the ID of the user to update
     * @param updatedUser the new data to apply
     * @return the updated User entity
     */
    public User update(Long id, User updatedUser) {
        validateId(id);

        @SuppressWarnings("null")
        User existing = repository.findById(id) 
        .orElseThrow(() -> new NotFoundException( ERR_NOT_FOUND, "User with ID " + id + " does not exist" )); 
        if (!existing.getEmail().equals(updatedUser.getEmail()) && repository.existsByEmail(updatedUser.getEmail())) { 
            throw new ConflictException( ERR_CONFLICT, "Email already exists" ); 
        }

        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        return repository.save(existing);
    }

    /**
     * validate that the id not null
     * 
     * @param id the ID of the user to validate
     */
    private void validateId(Long id) {
        if (id == null) {
            throw new BadRequestException(ERR_BAD_REQUEST, "ID cannot be null");
        }
    }
}