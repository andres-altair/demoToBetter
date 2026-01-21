package com.andres.demotobetter.modules.users.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * Data Transfer Object used for filtering a User.
 * @author andres
 */
@Getter
@Setter
public class UserFilterDTO {
    private String username;
    private String email;
}