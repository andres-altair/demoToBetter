package com.andres.demotobetter.modules.security.domain.service;


public interface TokenServicePort {
    String generateToken(String username); 
    String generateRefreshToken(String username);
    String extractUsername(String token);
    boolean isTokenValid(String token, String username);
}

