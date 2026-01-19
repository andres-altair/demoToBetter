package com.andres.demoToBetter.modules.users.repository;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import com.andres.demoToBetter.modules.users.model.User;

import java.util.Optional;


/**
 * Repository interface for managing User entities.
 * @author andres
 */
@Repository 
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks whether a user exists with the given username.
     *
     * @param username the username to check
     * @return true if a user with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks whether a user exists with the given email.
     *
     * @param email the email to check
     * @return true if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by its username.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by its email
     * 
     * @param email the email to search of
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByEmail (String email);
}
