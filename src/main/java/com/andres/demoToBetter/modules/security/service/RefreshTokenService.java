package com.andres.demotobetter.modules.security.service;

import com.andres.demotobetter.modules.security.entity.RefreshToken;
import com.andres.demotobetter.modules.security.entity.UserSecurity;

/**
 * Defines the contract for managing refresh tokens.
 * @author andres
 */
public interface RefreshTokenService {
    /**
     * Creates a new refresh token for the given user.
     * @param user the user
     * @return the created refresh token
     */
    RefreshToken create(UserSecurity user);

    /**
     * Rotates the refresh token by creating a new one and revoking the old one.
     * @param oldToken the old refresh token
     * @return the new refresh token
     */
    RefreshToken rotate(RefreshToken oldToken);

    /**
     * Validates the given refresh token.
     * @param token the refresh token
     * @return true if the token is valid, false otherwise
     */
    boolean validate(String token);

    /**
     * Revokes the given refresh token.
     * @param token the refresh token
     */
    void revoke(String token);

    /**
     * Gets the refresh token by the given token.
     * @param token the refresh token
     * @return the refresh token
     */
    RefreshToken getByToken(String token); 
}
