package com.andres.demotobetter.modules.users.infrastructure.persistence.entity;


import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.UserSecurityEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Entity representing a UserProfile in the system.
 * @author andres
 */

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "firstName is required")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Column(name = "last_name")
    private String lastName;

    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserSecurityEntity userSecurity; 
}