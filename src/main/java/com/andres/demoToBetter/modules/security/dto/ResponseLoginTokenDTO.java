package com.andres.demotobetter.modules.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object representing a  response login token.
 * @author andres
 */
@Data
@AllArgsConstructor
public class ResponseLoginTokenDTO {
    private String token;
}