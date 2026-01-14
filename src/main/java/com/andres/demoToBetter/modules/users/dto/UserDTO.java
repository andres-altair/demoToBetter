package com.andres.demoToBetter.modules.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a User
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO { 
    private Long id; 
    @NotBlank(message = "Username is required") 
    private String username; 
    @Email(message = "Email must be valid") 
    @NotBlank(message = "Email is required") 
    private String email; 
}