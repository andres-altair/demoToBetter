package com.andres.demotobetter.modules.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object representing a response login token.
 * 
 * @author andres
 */
@Data
@AllArgsConstructor
public class ResponseLoginTokenDTO {

    @Schema(description = "Access token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Refresh token JWT", example = "dGhpc19pc19hX3JlZnJlc2hfdG9rZW4")
    private String refreshToken;
}