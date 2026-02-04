package com.andres.demotobetter.modules.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a User
 * @author andres
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO { 
    private Long id; 
    private String firstName; 
    private String lastName;
    private String phone; 
    private String avatarUrl;
}