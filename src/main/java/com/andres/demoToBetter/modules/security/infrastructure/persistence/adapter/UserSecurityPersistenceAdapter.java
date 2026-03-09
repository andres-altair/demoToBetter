package com.andres.demotobetter.modules.security.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.andres.demotobetter.modules.security.domain.model.UserSecurity;
import com.andres.demotobetter.modules.security.domain.repository.UserSecurityRepositoryPort;
import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.UserSecurityEntity;
import com.andres.demotobetter.modules.security.infrastructure.persistence.mapper.UserSecurityPersistenceMapper;
import com.andres.demotobetter.modules.security.infrastructure.persistence.repository.UserSecurityJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSecurityPersistenceAdapter implements UserSecurityRepositoryPort {
    private final UserSecurityJpaRepository jpaRepository;
    private final UserSecurityPersistenceMapper mapper; 

    @Override
    public UserSecurity save(UserSecurity domain) {
        UserSecurityEntity entity = mapper.toEntity(domain);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<UserSecurity> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<UserSecurity> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}


