package com.andres.demotobetter.modules.security.domain.service;

/**
 * Puerto para manejar la identidad sin acoplarse a Spring Security.
 */
public interface IdentityManagerPort {
    /**
     * Valida las credenciales del usuario. 
     * Lanza una excepción si la autenticación falla.
     */
    void authenticate(String email, String password);
}
