package com.andres.demotobetter.modules.users.spec;

import org.springframework.data.jpa.domain.Specification;

import com.andres.demotobetter.modules.users.entity.UserProfile;

/**
 * Specifications class for filtering UserProfile entities.
 * @author andres
 */
public class UserProfileSpecification {

    private UserProfileSpecification() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<UserProfile> firstNameContains(String firstName) {
        return (root, query, cb) -> (firstName == null || firstName.isBlank()) ? null
                : cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<UserProfile> lastNameContains(String lastName) {
        return (root, query, cb) -> (lastName == null || lastName.isBlank()) ? null
                : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<UserProfile> phoneContains(String phone) {
        return (root, query, cb) -> (phone == null || phone.isBlank()) ? null
                : cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
    }
}