package com.andres.demoToBetter.modules.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email; 
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used for creating a new User.
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO { 
    @NotBlank(message = "Username is required") 
    private String username; 
    @Email(message = "Email must be valid") 
    @NotBlank(message = "Email is required") 
    private String email; 
}