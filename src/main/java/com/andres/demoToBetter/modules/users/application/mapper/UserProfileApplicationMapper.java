package com.andres.demotobetter.modules.users.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.andres.demotobetter.modules.users.application.dto.*;
import com.andres.demotobetter.modules.users.domain.model.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "securityId", ignore = true)
    UserProfile toDomain(UserProfileCreateDTO dto);

    UserProfileDTO toDTO(UserProfile domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "securityId", ignore = true)
    void updateDomainFromDto(@MappingTarget UserProfile domain, UserProfileUpdateDTO dto);
}
