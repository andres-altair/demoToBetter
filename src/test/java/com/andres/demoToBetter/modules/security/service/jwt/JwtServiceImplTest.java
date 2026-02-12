package com.andres.demotobetter.modules.security.service.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class JwtServiceImplTest {
    @Mock
    private UserDetails userDetails;
    @InjectMocks

    private JwtServiceImpl jwtService;

    private final String secret = "321895743890165780436t5781436t578436332";
    private final long expiration = 3600000;
    private final String username = "andres@mail.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", expiration);
    }

    @Test
    void generateToken_WhenUserValid_ReturnsToken() {
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void generateRefreshToken_WhenTokenValid_ReturnsRefreshToken() {
        String token = jwtService.generateRefreshToken(username);

        assertNotNull(token);
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void extractAllClaims_WhenTokenValid_ReturnsClaims() {
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtService.generateToken(userDetails);

        Claims claims = jwtService.extractAllClaims(token);

        assertEquals(username, claims.getSubject());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void extractUsername_WhenTokenValid_ReturnsUsername() {
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(userDetails);
        String extracted = jwtService.extractUsername(token);

        assertEquals(username, extracted);
    }

    @Test
    void isTokenValid_WhenValidToken_ReturnsTrue() {
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_WhenUsernameDoesNotMatch_ReturnsFalse() {
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUser = mock(UserDetails.class);
        when(otherUser.getUsername()).thenReturn("wrong@mail.com");

        boolean isValid = jwtService.isTokenValid(token, otherUser);

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_WhenTokenExpired_ReturnsFalse() {
        ReflectionTestUtils.setField(jwtService, "expiration", -10000L);
        when(userDetails.getUsername()).thenReturn(username);

        String expiredToken = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(expiredToken, userDetails);

        assertFalse(isValid);
    }
}