package com.andres.demotobetter.modules.security.domain.service;

/**
 * Interface for password hashing operations.
 * 
 * @author andres
 */
public interface PasswordHasherPort {
    String encode(String rawPassword);
}
