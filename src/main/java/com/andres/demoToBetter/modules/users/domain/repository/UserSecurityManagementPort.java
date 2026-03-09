package com.andres.demotobetter.modules.users.domain.repository;

import java.util.Set;

public interface UserSecurityManagementPort {
    Long createSecurityUser(String email, String password, Set<String> roles);
    void disableUser(Long id);
}

