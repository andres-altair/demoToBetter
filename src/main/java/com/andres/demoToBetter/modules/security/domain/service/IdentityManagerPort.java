package com.andres.demotobetter.modules.security.domain.service;

/**
 * Interface for identity management operations.
 */
public interface IdentityManagerPort {
    void authenticate(String email, String password);
}
