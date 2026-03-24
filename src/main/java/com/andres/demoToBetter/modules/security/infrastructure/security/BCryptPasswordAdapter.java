package com.andres.demotobetter.modules.security.infrastructure.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.andres.demotobetter.modules.security.domain.service.PasswordHasherPort;

import lombok.RequiredArgsConstructor;

/**
 * Adapter class for PasswordHasherPort using BCrypt.
 * 
 * @author andres
 */
@Component
@RequiredArgsConstructor
public class BCryptPasswordAdapter implements PasswordHasherPort {
    private final PasswordEncoder passwordEncoder; 
    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}

