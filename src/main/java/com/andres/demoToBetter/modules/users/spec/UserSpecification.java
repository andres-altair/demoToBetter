package com.andres.demotobetter.modules.users.spec;

import org.springframework.data.jpa.domain.Specification;

import com.andres.demotobetter.modules.users.model.UserProfile;

/**
 * Utility class providing reusable Specifications for filtering UserProfile entities.
 * @author andres
 */
public class UserSpecification {

    private UserSpecification() { throw new UnsupportedOperationException("Utility class"); }
    /**
     * Returns a Specification that filters by firstName containing the given text.
     *
     * @param firstName partial value to match
     */
    public static Specification<UserProfile> firstNameContains(String firstName){
        return (root, query, cb)-> (firstName == null || firstName.isBlank()) ? null :
            cb.like(cb.lower(root.get("firstName")),"%" + firstName.toLowerCase()+ "%");
    }

    /**
     * Returns a Specification that filters by lastName containing the given text.
     *
     * @param lastName partial value to match
     */
    public static Specification<UserProfile> lastNameContains (String lastName){
        return (root, query, cb) -> (lastName == null || lastName.isBlank()) ? null : 
            cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }


    /**
     * Returns a Specification that filters by phone containing the given text.
     *
     * @param phone partial value to match
     */
    public static Specification<UserProfile> phoneContains (String phone){
        return (root, query, cb) -> (phone == null || phone.isBlank()) ? null : 
            cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
    }
}