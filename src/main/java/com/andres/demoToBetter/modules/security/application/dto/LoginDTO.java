package com.andres.demotobetter.modules.security.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object used for logging in a user.
 * 
 * @author andres
 */
@AllArgsConstructor
@Data
public class LoginDTO {

    @Schema(description = "User's email", example = "user1@example.com")
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "User password", example = "passs")
    @NotBlank(message = "Password is required")
    private String password;
}