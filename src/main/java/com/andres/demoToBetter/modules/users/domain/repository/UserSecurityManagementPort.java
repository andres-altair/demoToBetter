package com.andres.demotobetter.modules.users.domain.repository;

import java.util.Set;
/**
 * Interface for UserSecurity management operations.
 * 
 * @author andres
 */
public interface UserSecurityManagementPort {
    Long createSecurityUser(String email, String password, Set<String> roles);
    void disableUser(Long id);
}

