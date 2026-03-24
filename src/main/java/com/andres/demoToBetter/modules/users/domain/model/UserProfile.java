package com.andres.demotobetter.modules.users.domain.model;

import lombok.*;
/**
 * Class representing a user profile.
 * 
 * @author andres
 */
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatarUrl;
    private Long securityId; 
}
