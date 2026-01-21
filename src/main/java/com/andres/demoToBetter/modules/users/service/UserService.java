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
 * Service layer responsible for handling business logic related to User entities.
 * @author andres
 */
@Service
@AllArgsConstructor
public class UserService { 
    private static final int MAX_PAGE_SIZE = 50;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "username", "email");

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
            throw new BadRequestException("USR_400","Page size cannot exceed " + MAX_PAGE_SIZE);
        }

        if (pageable.getPageNumber() < 0) {
            throw new BadRequestException("USR_400","Page number cannot be negative");
        }

        pageable.getSort().forEach(order -> {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new BadRequestException("USR_400","Sorting by '" + order.getProperty() + "' is not allowed");
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
    public Optional<User> findById(Long id) { 
        if (id == null) {
            throw new BadRequestException("USR_400", "ID cannot be null");
        }

        return repository.findById(id); 
    } 

    /**
     * Retrieves a user by its username.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, otherwise empty
     */
    public Optional<User> findByUsername(String username) { 
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
        throw new ConflictException( "USR_409", "Email already exists: " + user.getEmail() );
    }
    return repository.save(user);
    }


    /**
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete
     */
    public void delete(Long id) { 
        if (id == null) {
            throw new BadRequestException("USR_400", "ID cannot be null");
        }

        if (!repository.existsById(id)) {
            throw new NotFoundException( "USR_404", "User with ID " + id + " does not exist" );
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
        if (id == null) {
            throw new BadRequestException("USR_400", "ID cannot be null");
        }

        User existing = repository.findById(id) 
        .orElseThrow(() -> new NotFoundException( "USR_404", "User with ID " + id + " does not exist" )); 
        if (!existing.getEmail().equals(updatedUser.getEmail()) && repository.existsByEmail(updatedUser.getEmail())) { 
            throw new ConflictException( "USR_409", "Email already exists: " + updatedUser.getEmail() ); 
        }
        
        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        return repository.save(existing);
    }
}