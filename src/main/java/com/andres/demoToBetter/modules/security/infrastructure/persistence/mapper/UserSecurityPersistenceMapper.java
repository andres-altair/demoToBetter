package com.andres.demotobetter.modules.security.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import com.andres.demotobetter.modules.security.domain.model.UserSecurity;
import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.UserSecurityEntity;

@Mapper(componentModel = "spring")
public interface UserSecurityPersistenceMapper {

    UserSecurity toDomain(UserSecurityEntity entity);

    UserSecurityEntity toEntity(UserSecurity domain);
}
