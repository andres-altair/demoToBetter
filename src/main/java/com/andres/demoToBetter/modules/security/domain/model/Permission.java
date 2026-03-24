package com.andres.demotobetter.modules.security.domain.model;

import lombok.*;
/**
 * Class representing a permission in the system.
 * @author andres
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Long id;
    private String name;
}