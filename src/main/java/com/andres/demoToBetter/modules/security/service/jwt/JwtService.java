package com.andres.demotobetter.modules.security.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

/**
 * Defines the contract for managing JWT tokens.
 * @author andres
 */
public interface JwtService {
    /**
     * Generates a JWT token for the given user.
     */
    public String generateToken(UserDetails userDetails);
    /**
     * Generates a refresh token for the given user.
     */
    public String generateRefreshToken(String username);
    /**
     * Extracts all claims from the given token.
     */
    public Claims extractAllClaims(String token) ;  
    /**
     * Extracts the username from the given token.
     */
    public String extractUsername(String token);
    /**
     * Checks if the given token is valid for the given user.
     */
    public boolean isTokenValid(String token, UserDetails userDetails);
}
