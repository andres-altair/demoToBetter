package com.andres.demotobetter.modules.security.domain.model;

import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;
    private Set<Permission> permissions = new HashSet<>();
}