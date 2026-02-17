package com.andres.demotobetter.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Nombre del usuario", example = "Andrés")
    @NotBlank(message = "firstName is required")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Molina")
    @NotBlank(message = "lastName is required")
    private String lastName;

    @Schema(description = "Número de teléfono del usuario", example = "+34 600 123 456")
    @NotBlank(message = "phone is required")
    private String phone;

    @Schema(description = "URL del avatar del usuario", example = "https://cdn.site.com/avatar.png")
    private String avatarUrl;
}