package com.andres.demotobetter.modules.users.spec;

import org.springframework.data.jpa.domain.Specification;

import com.andres.demotobetter.modules.users.model.User;

/**
 * Utility class containing reusable Specifications for filtering User entities.
 * @author andres
 */
public class UserSpecification {

    private UserSpecification() { throw new UnsupportedOperationException("Utility class"); }
    /**
     * Creates a Specification that filters users whose username contains
     * 
     * @param username partial text to search within the username field
     * @return a Specification for filtering by username, or null if not applicable
     */
    public static Specification<User> usernameContains(String username){
        return (root, query, cb)-> (username == null || username.isBlank()) ? null :
            cb.like(cb.lower(root.get("username")),"%" + username.toLowerCase()+ "%");
    }

    /**
     * Creates a Specification that filters users whose email contains
     * 
     * @param email partial text to search within the email field
     * @return a Specification for filtering by email, or null if not applicable
     */
    public static Specification<User> emailContains (String email){
        return (root, query, cb) -> (email == null || email.isBlank()) ? null : 
            cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
}