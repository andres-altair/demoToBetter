package com.andres.demotobetter.modules.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object used for updating a new User.
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDTO {
    @NotBlank(message = "firstName is required") 
    private String firstName; 
    @NotBlank(message = "lastName is required") 
    private String lastName;
    @NotBlank(message = "phone is required")
    private String phone; 
    private String avatarUrl;
}
