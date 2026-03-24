package com.andres.demotobetter.modules.security.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import com.andres.demotobetter.modules.security.domain.model.RefreshToken;
import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.RefreshTokenEntity;

@Mapper(componentModel = "spring", uses = { UserSecurityPersistenceMapper.class })
public interface RefreshTokenPersistenceMapper {

    RefreshToken toDomain(RefreshTokenEntity entity);
    RefreshTokenEntity toEntity(RefreshToken domain);
}
