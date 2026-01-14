package com.andres.demoToBetter.modules.users.mapper;

import com.andres.demoToBetter.modules.users.dto.UserCreateDTO;
import com.andres.demoToBetter.modules.users.dto.UserDTO;
import com.andres.demoToBetter.modules.users.dto.UserUpdateDTO;
import com.andres.demoToBetter.modules.users.model.User;

/**
 * Utility class responsible for converting between User entities and DTOs.
 * @author andres
 */
public class UserMapper {

    /**
     * Converts a User entity into a UserDTO.
     * This method is used when returning user data to the client.
     *
     * @param user the User entity to convert
     * @return a UserDTO containing the mapped fields
     */
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    /**
     * Converts a UserCreateDTO into a User entity.
     * This method is used when creating a new user from client input.
     *
     * @param dto the UserCreateDTO containing the input data
     * @return a User entity populated with the provided values
     */
    public static User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        return user;
    }

    /**
     * Converts a UserUpdateDTO into a User entity.
     * This method is used during update operations.
     * 
     * @param dto the UserUpdateDTO containing updated fields
     * @return a User entity with updated values (without ID)
     */
    public static User toEntity(UserUpdateDTO dto) {
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    return user;
}

}
