package com.andres.demotobetter.modules.users.application.dto;

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

    @Schema(description = "Unique user ID", example = "15")
    private Long id;

    @Schema(description = "User's first name", example = "Andrés")
    private String firstName;

    @Schema(description = "User's last name", example = "Molina")
    private String lastName;

    @Schema(description = "Phone number", example = "+34 600 123 456")
    private String phone;

    @Schema(description = "URL of the user's avatar", example = "https://cdn.site.com/avatar.png")
    private String avatarUrl;
}
