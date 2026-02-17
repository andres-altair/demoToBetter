package com.andres.demotobetter.modules.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used for creating a new User.
 * 
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCreateDTO {
    @Schema(description = "Correo electrónico del usuario", example = "andres@mail.com")
    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;
    @Schema(description = "Contraseña del usuario", example = "StrongPass123!")
    @NotBlank(message = "password is required")
    private String password;
    @Schema(description = "Roles asignados al usuario", example = "[\"ADMIN\", \"USER\"]")
    private Set<String> roles;
    @Schema(description = "Nombre del usuario", example = "Andrés")
    @NotBlank(message = "firstName is required")
    private String firstName;
    @Schema(description = "Apellido del usuario", example = "Molina")
    @NotBlank(message = "lastName is required")
    private String lastName;
    @Schema(description = "Número de teléfono", example = "+34 600 123 456")
    @NotBlank(message = "phone is required")
    private String phone;
    @Schema(description = "URL del avatar del usuario", example = "https://cdn.site.com/avatar.png")
    private String avatarUrl;
}