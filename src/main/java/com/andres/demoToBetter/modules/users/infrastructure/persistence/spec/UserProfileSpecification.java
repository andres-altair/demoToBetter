package com.andres.demotobetter.modules.users.infrastructure.persistence.spec;

import org.springframework.data.jpa.domain.Specification;

import com.andres.demotobetter.modules.users.infrastructure.persistence.entity.UserProfileEntity;

/**
 * Specifications class for filtering UserProfile entities.
 * @author andres
 */
public class UserProfileSpecification {

    private UserProfileSpecification() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<UserProfileEntity> firstNameContains(String firstName) {
        return (root, query, cb) -> (firstName == null || firstName.isBlank()) ? null
                : cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<UserProfileEntity> lastNameContains(String lastName) {
        return (root, query, cb) -> (lastName == null || lastName.isBlank()) ? null
                : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<UserProfileEntity> phoneContains(String phone) {
        return (root, query, cb) -> (phone == null || phone.isBlank()) ? null
                : cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
    }
}