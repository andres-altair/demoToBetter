package com.andres.demotobetter.modules.security.service;

import java.util.Set;

import com.andres.demotobetter.modules.security.entity.Role;

public interface RoleService {
    Set<Role> resolveRoles(Set<String> roleNames);
}

