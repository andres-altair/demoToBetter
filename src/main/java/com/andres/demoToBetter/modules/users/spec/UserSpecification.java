package com.andres.demoToBetter.modules.users.spec;

import com.andres.demoToBetter.modules.users.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> usernameContains(String username){
        return (root, query, cb)-> (username == null || username.isBlank()) ? null :
            cb.like(cb.lower(root.get("username")),"%" + username.toLowerCase()+ "%");
    }

    public static Specification<User> emailContains (String email){
        return (root, query, cb) -> (email == null || email.isBlank()) ? null : 
            cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
}