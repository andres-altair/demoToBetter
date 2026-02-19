package com.andres.demotobetter.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object used for filtering a User.
 * 
 * @author andres
 */
@Getter
@Setter
public class UserProfileFilterDTO {

    @Schema(description = "Filter by username", example = "Andrés")
    private String firstName;

    @Schema(description = "Filter by last name", example = "Molina")
    private String lastName;

    @Schema(description = "Filtre by phone", example = "+34 600 123 456")
    private String phone;
}