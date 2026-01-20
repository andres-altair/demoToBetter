package com.andres.demoToBetter.modules.users.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.andres.demoToBetter.common.exception.custom.ConflictException;
import com.andres.demoToBetter.common.exception.custom.NotFoundException;
import com.andres.demoToBetter.modules.users.model.User;
import com.andres.demoToBetter.modules.users.repository.UserRepository;

import lombok.AllArgsConstructor;

/**
 * Service layer responsible for handling business logic related to User entities.
 * @author andres
 */
@Service
@AllArgsConstructor
public class UserService { 
    
    private final UserRepository repository; 

    /**
     * Retrieves all users from the database.
     *
     * @return a list of User entities
     */
    public List<User> findAll() { 
        return repository.findAll(); 
    } 

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user
     * @return an Optional containing the User if found, otherwise empty
     */
    public Optional<User> findById(Long id) { 
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
     * If the user does not exist, a RuntimeException is thrown.
     *
     * @param id the ID of the user to delete
     */
    public void delete(Long id) { 
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