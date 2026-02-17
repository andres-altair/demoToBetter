package com.andres.demotobetter.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a User
 * 
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    @Schema(description = "ID único del usuario", example = "15")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Andrés")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Molina")
    private String lastName;

    @Schema(description = "Número de teléfono", example = "+34 600 123 456")
    private String phone;

    @Schema(description = "URL del avatar del usuario", example = "https://cdn.site.com/avatar.png")
    private String avatarUrl;
}
