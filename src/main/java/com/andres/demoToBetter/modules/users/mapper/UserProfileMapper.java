package com.andres.demotobetter.modules.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.andres.demotobetter.modules.users.dto.UserProfileCreateDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileUpdateDTO;
import com.andres.demotobetter.modules.users.model.UserProfile;

/**
 * Utility class responsible for converting between UserProfile entities and DTOs.
 * 
 * @author andres
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileDTO toDTO(UserProfile userProfile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userSecurity", ignore = true)
    UserProfile toEntity(UserProfileCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userSecurity", ignore = true)
    UserProfile toEntity(UserProfileUpdateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userSecurity", ignore = true)
    void updateEntity(@MappingTarget UserProfile entity, UserProfileUpdateDTO dto);
}