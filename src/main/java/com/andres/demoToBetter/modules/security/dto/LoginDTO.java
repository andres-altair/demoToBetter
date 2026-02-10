package com.andres.demotobetter.modules.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * Data Transfer Object used for logging in a user.
 * @author andres
 */
@AllArgsConstructor
@Data
public class LoginDTO {
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
