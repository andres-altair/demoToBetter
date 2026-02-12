package com.andres.demotobetter.modules.security.service;

import java.util.Set;

import com.andres.demotobetter.modules.security.entity.UserSecurity;

/**
 * Defines the contract for managing user security.
 * @author andres
 */
public interface UserSecurityService {
    /**
     * Creates a new security user.
     */
    UserSecurity createSecurityUser(String email, String password, Set<String> roles);

    void disableUser(Long id);

}