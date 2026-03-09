package com.andres.demotobetter.modules.users.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.andres.demotobetter.modules.users.domain.model.UserProfile;
import com.andres.demotobetter.modules.users.infrastructure.persistence.entity.UserProfileEntity;

@Mapper(componentModel = "spring")
public interface UserProfilePersistenceMapper {
    @Mapping(target = "userSecurity", ignore = true) 
    UserProfileEntity toEntity(UserProfile domain);
    
    @Mapping(target = "securityId", ignore = true)
    UserProfile toDomain(UserProfileEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userSecurity", ignore = true)
    void updateEntityFromDomain(UserProfile domain, @MappingTarget UserProfileEntity entity);
}

