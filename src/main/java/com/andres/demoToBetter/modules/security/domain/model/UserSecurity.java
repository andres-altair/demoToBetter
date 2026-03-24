package com.andres.demotobetter.modules.security.domain.model;

import java.util.HashSet;
import java.util.Set;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Class representing a user in the system.
 * 
 * @author andres
 */
public class UserSecurity {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean active = true;
    private Set<Role> roles = new HashSet<>();
}