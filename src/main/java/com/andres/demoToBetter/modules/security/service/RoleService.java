package com.andres.demotobetter.modules.security.service;

import java.util.Set;

import com.andres.demotobetter.modules.security.entity.Role;

/**
 * Defines the contract for managing roles.
 * @author andres
 */
public interface RoleService {
    /**
     * Transforms a set of role names into a set of roles.
     */
    Set<Role> resolveRoles(Set<String> roleNames);
}

