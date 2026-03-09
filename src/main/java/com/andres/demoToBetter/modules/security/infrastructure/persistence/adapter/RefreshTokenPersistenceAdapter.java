package com.andres.demotobetter.modules.security.infrastructure.persistence.adapter;

import com.andres.demotobetter.modules.security.domain.model.RefreshToken;
import com.andres.demotobetter.modules.security.domain.repository.RefreshTokenPort;
import com.andres.demotobetter.modules.security.infrastructure.persistence.mapper.RefreshTokenPersistenceMapper;
import com.andres.demotobetter.modules.security.infrastructure.persistence.repository.RefreshTokenJpaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

// modules/security/infrastructure/persistence/adapter/AuthRefreshTokenPersistenceAdapter.java
@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenPort {
    private final RefreshTokenJpaRepository repository;
    private final RefreshTokenPersistenceMapper mapper;

    @Override
    public RefreshToken save(RefreshToken domain) {
        return mapper.toDomain(repository.save(mapper.toEntity(domain)));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token).map(mapper::toDomain);
    }
}
