package com.andres.demotobetter.modules.security.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.entity.Role;
import com.andres.demotobetter.modules.security.repository.RoleRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class that implements RoleService.
 * 
 * @author andres
 */
@Service
@AllArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private static final String ERR_BAD_REQUEST = "ERR_BAD_REQUEST";
    private final RoleRepository roleRepository;

    @Override
    public Set<Role> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            log.debug("No roles were provided. Assigning default role: USER");
            return Set.of(
                    roleRepository.findByName("USER")
                            .orElseThrow(() -> {
                                log.error("CRITICAL ERROR: USER role not found in the database");
                                return new BadRequestException(ERR_BAD_REQUEST, "Role USER not found");
                            }));
        }

        log.debug("Resolving role names: {}", roleNames);
        return roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> {
                            log.warn("Attempt to assign a non-existent role: {}", name);
                            return new BadRequestException(ERR_BAD_REQUEST, "Role '" + name + "' not found");
                        }))
                .collect(Collectors.toSet());
    }
}
