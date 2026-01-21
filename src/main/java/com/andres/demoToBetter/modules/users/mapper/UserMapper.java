package com.andres.demotobetter.modules.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.andres.demotobetter.modules.users.dto.UserCreateDTO;
import com.andres.demotobetter.modules.users.dto.UserDTO;
import com.andres.demotobetter.modules.users.dto.UserUpdateDTO;
import com.andres.demotobetter.modules.users.model.User;

/**
 * Utility class responsible for converting between User entities and DTOs.
 * 
 * @author andres
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Converts a User entity into a UserDTO.
     *
     * @param user the User entity to convert
     * @return a UserDTO containing the mapped fields
     */
    UserDTO toDTO(User user);

    /**
     * Converts a UserCreateDTO into a new User entity.
     *
     * @param dto the DTO containing the input data
     * @return a new User entity populated with the provided values
     */
    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateDTO dto);

    /**
     * Converts a UserUpdateDTO into a User entity.
     *
     * @param dto the DTO containing updated fields
     * @return a User entity with updated values (without ID)
     */
    @Mapping(target = "id", ignore = true)
    User toEntity(UserUpdateDTO dto); 

    /**
     * Updates an existing User entity with values from a UserUpdateDTO.
     * 
     * @param entity the existing User entity to update
     * @param dto the DTO containing new values
     */
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget User entity, UserUpdateDTO dto);
}