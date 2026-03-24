package com.andres.demotobetter.modules.security.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.andres.demotobetter.modules.security.domain.model.Role;
import com.andres.demotobetter.modules.security.domain.repository.RoleRepositoryPort;
import com.andres.demotobetter.modules.security.infrastructure.persistence.mapper.RolePersistenceMapper;
import com.andres.demotobetter.modules.security.infrastructure.persistence.repository.RoleJpaRepository;

import lombok.RequiredArgsConstructor;
/**
 * Adapter class for Role persistence operations.
 * 
 * @author andres
 */
@Component
@RequiredArgsConstructor
public class RolePersistenceAdapter implements RoleRepositoryPort {
    private final RoleJpaRepository jpaRepository;
    private final RolePersistenceMapper mapper; 

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name).map(mapper::toDomain);
    }
}

