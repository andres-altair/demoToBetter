package com.andres.demotobetter.modules.security.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.entity.Role;
import com.andres.demotobetter.modules.security.repository.RoleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final String ERR_BAD_REQUEST = "ERR_BAD_REQUEST";
    private final RoleRepository roleRepository;
    @Override
    public Set<Role> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) { 
            return Set.of( 
                roleRepository.findByName("USER") 
                .orElseThrow(() -> new BadRequestException(ERR_BAD_REQUEST, "Role USER not found")) ); 
        }
        return roleNames.stream()
        .map(name -> roleRepository.findByName(name)
            .orElseThrow(() -> new BadRequestException(ERR_BAD_REQUEST, "Role '" + name + "' not found")))
        .collect(Collectors.toSet());
    }
}
