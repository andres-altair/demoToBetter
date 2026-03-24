package com.andres.demotobetter.modules.security.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.andres.demotobetter.modules.security.domain.model.Role;
import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.RoleEntity;

@Mapper(componentModel = "spring")
public interface RolePersistenceMapper {
    Role toDomain(RoleEntity entity);
    RoleEntity toEntity(Role domain);
}

