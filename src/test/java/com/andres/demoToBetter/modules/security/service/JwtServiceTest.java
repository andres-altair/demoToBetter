package com.andres.demotobetter.modules.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService;

    @SuppressWarnings("null")
    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(jwtService, "secret", "12345678901234567890123456789012");
        ReflectionTestUtils.setField(jwtService, "expiration", 60000L);
    }

    private UserDetails buildUser(String username) {
        return User.withUsername(username).password("pass").roles("USER").build();
    }

    @Test
    void extractAllClaims_WhenTokenValid_ReturnsClaims() {
        UserDetails user = buildUser("andres");

        String token = jwtService.generateToken(user);
        Claims claims = jwtService.extractAllClaims(token);

        assertEquals("andres", claims.getSubject());
        assertNotNull(claims.getExpiration());
        assertNotNull(claims.getIssuedAt());
    }

    @Test
    void extractUsername_WhenTokenValid_ReturnsUsername() {
        UserDetails user = buildUser("andres");

        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);

        assertEquals("andres", username);
    }

    @Test
    void generateToken_WhenUserValid_ReturnsToken() {
        UserDetails user = buildUser("andres");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
    }

    @Test
    void isTokenValid_WhenTokenAndUserValid_ReturnsTrue() {
        UserDetails user = buildUser("andres");

        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_WhenUsernameDoesNotMatch_ReturnsFalse() {
        UserDetails user = buildUser("andres");

        UserDetails user2 = buildUser("otro");

        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(token, user2);

        assertFalse(isValid);
    }

    @SuppressWarnings("null")
    @Test
    void isTokenValid_WhenTokenExpired_ReturnsFalse() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);

        UserDetails user = buildUser("andres");

        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertFalse(isValid);
    }

    @SuppressWarnings("null")
    @Test
    void isTokenValid_WhenUsernameDoesNotMatchAndTokenExpired_ReturnsFalse() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);

        UserDetails user = buildUser("andres");
        UserDetails user2 = buildUser("otro");

        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user2);

        assertFalse(isValid);
    }
}