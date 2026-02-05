package com.andres.demotobetter.modules.security.service;

import java.util.Set;

import com.andres.demotobetter.modules.security.entity.UserSecurity;

public interface UserSecurityService {
    UserSecurity createSecurityUser(String email, String password, Set<String> roles);
}