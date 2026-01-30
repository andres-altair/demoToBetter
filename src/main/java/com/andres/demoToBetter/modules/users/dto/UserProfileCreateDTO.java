package com.andres.demotobetter.modules.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used for creating a new User.
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCreateDTO { 
    @NotBlank(message = "firstName is required") 
    private String firstName; 
    @NotBlank(message = "lastName is required") 
    private String lastName;
    @NotBlank(message = "phone is required")
    private String phone; 
    private String avatarUrl;
}