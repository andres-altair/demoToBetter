package com.andres.demotobetter.modules.security.service;

import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.ResponseLoginTokenDTO;

/**
 * Defines the contract for managing authentication.
 * @author andres
 */
public interface AuthService {
    /**
     * Manages the login process.
     */
    public ResponseLoginTokenDTO login(LoginDTO loginDTO);

    /**
     * Manages the refresh token process.
     */
    public ResponseLoginTokenDTO refresh(String refreshToken);
}
