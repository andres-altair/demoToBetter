package com.andres.demotobetter.modules.security.application.usecase;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.domain.BadRequestException;
import com.andres.demotobetter.modules.security.domain.model.Role;
import com.andres.demotobetter.modules.security.domain.repository.RoleRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * Service class for resolving and validating role entities based on their names.
 * 
 * @author andres
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ResolveRolesUseCase {
    private final RoleRepositoryPort roleRepository;

    private static final String ERR_BAD_REQUEST = "USR_400";


    public Set<Role> execute(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Set.of(fetchRole("USER"));
        }

        return roleNames.stream()
                .map(this::fetchRole)
                .collect(Collectors.toSet());
    }

    private Role fetchRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> {
                            log.warn("Attempt to assign a non-existent role: {}", name);
                            return new BadRequestException(ERR_BAD_REQUEST, "Role '" + name + "' not found");
                        });
    }
}

