package com.andres.demotobetter.modules.users.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.andres.demotobetter.common.domain.NotFoundException;
import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.UserSecurityEntity;
import com.andres.demotobetter.modules.users.domain.model.PageQuery;
import com.andres.demotobetter.modules.users.domain.model.PageResponse;
import com.andres.demotobetter.modules.users.domain.model.UserProfile;
import com.andres.demotobetter.modules.users.domain.model.UserProfileFilter;
import com.andres.demotobetter.modules.users.domain.repository.UserProfileRepositoryPort;
import com.andres.demotobetter.modules.users.infrastructure.persistence.entity.UserProfileEntity;
import com.andres.demotobetter.modules.users.infrastructure.persistence.mapper.UserProfilePersistenceMapper;
import com.andres.demotobetter.modules.users.infrastructure.persistence.repository.UserProfileJpaRepository;
import com.andres.demotobetter.modules.users.infrastructure.persistence.spec.UserProfileSpecification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
/**
 * Adapter class for UserProfile persistence operations.    
 * 
 * @author andres
 */
@Component
@RequiredArgsConstructor
public class UserProfilePersistenceAdapter implements UserProfileRepositoryPort {

    @PersistenceContext
    private final EntityManager entityManager;

    private final UserProfileJpaRepository jpaRepository;
    private final UserProfilePersistenceMapper mapper;
    private static final String ERR_NOT_FOUND = "USR_404";


    @Override
    public UserProfile save(UserProfile domain) {
        var entity = mapper.toEntity(domain);
        entity.setUserSecurity(entityManager.getReference(UserSecurityEntity.class, domain.getSecurityId()));

        UserProfileEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<UserProfile> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PageResponse<UserProfile> findAll(UserProfileFilter filter, PageQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.direction());
        Pageable pageable = PageRequest.of(query.page(), query.size(), Sort.by(direction, query.sortBy()));

        Specification<UserProfileEntity> spec = Specification.allOf(
                UserProfileSpecification.firstNameContains(filter.firstName()),
                UserProfileSpecification.lastNameContains(filter.lastName()),
                UserProfileSpecification.phoneContains(filter.phone()));

        Page<UserProfileEntity> result = jpaRepository.findAll(spec, pageable);

        return new PageResponse<>(
                result.getContent().stream().map(mapper::toDomain).toList(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Override
    public UserProfile update(UserProfile domain) {
        UserProfileEntity entity = jpaRepository.findById(domain.getId())
                .orElseThrow(() -> new NotFoundException(ERR_NOT_FOUND, "Profile not found"));

        mapper.updateEntityFromDomain(domain, entity);

        return mapper.toDomain(jpaRepository.save(entity));
    }
}
