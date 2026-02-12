package com.andres.demotobetter.modules.security.service.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.entity.RefreshToken;
import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private final long refreshExpiration = 604800000;
    private UserSecurity user;

    @SuppressWarnings("null")
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshExpiration", refreshExpiration);
        user = new UserSecurity();
        user.setEmail("andres@mail.com");
    }

    @SuppressWarnings("null")
    @Test
    void create_WhenUserValid_ReturnsToken() {
        RefreshToken saved = new RefreshToken();
        saved.setToken("refresh-token");
        saved.setUser(user);
        saved.setExpiryDate(new Date(System.currentTimeMillis() + refreshExpiration));

        when(jwtService.generateRefreshToken(user.getEmail())).thenReturn("refresh-token");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(saved);

        RefreshToken result = refreshTokenService.create(user);

        assertEquals("refresh-token", result.getToken());
        assertEquals(user, result.getUser());
        assertNotNull(result.getExpiryDate());
        verify(jwtService).generateRefreshToken(user.getEmail());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void validate_WhenTokenIsValid_ReturnsTrue() {
        String tokenStr = "valid-token";
        RefreshToken rt = new RefreshToken();
        rt.setRevoked(false);
        rt.setExpiryDate(new Date(System.currentTimeMillis() + 10000)); // Futuro

        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(rt));

        boolean isValid = refreshTokenService.validate(tokenStr);

        assertTrue(isValid);
    }

    @Test
    void validate_WhenTokenIsRevoked_ReturnsFalse() {
        String tokenStr = "revoked-token";
        RefreshToken rt = new RefreshToken();
        rt.setRevoked(true);
        rt.setExpiryDate(new Date(System.currentTimeMillis() + 10000));

        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(rt));

        boolean isValid = refreshTokenService.validate(tokenStr);

        assertFalse(isValid);
    }

    @Test
    void validate_WhenTokenExpired_ReturnsFalse() {
        String tokenStr = "revoked-token";
        RefreshToken rt = new RefreshToken();
        rt.setRevoked(false);
        rt.setExpiryDate(new Date(System.currentTimeMillis() - 10000));

        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(rt));

        boolean isValid = refreshTokenService.validate(tokenStr);

        assertFalse(isValid);
    }

    @SuppressWarnings("null")
    @Test
    void rotate_WhenValidOldToken_ReturnsNewToken() {
        RefreshToken oldToken = new RefreshToken();
        oldToken.setUser(user);
        oldToken.setRevoked(false);
        oldToken.setExpiryDate(new Date(System.currentTimeMillis() + 10000));
        RefreshToken newToken = new RefreshToken();
        newToken.setToken("new-refresh-token");
        newToken.setUser(user);

        when(jwtService.generateRefreshToken(user.getEmail())).thenReturn("new-refresh-token");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(newToken);

        RefreshToken result = refreshTokenService.rotate(oldToken);

        assertEquals("new-refresh-token", result.getToken());
        assertEquals(user, result.getUser());
        assertFalse(result.isRevoked());
        verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class));
    }

    @Test
    void revoke_WhenTokenExists_SetsRevokedTrue() {
        RefreshToken token = new RefreshToken();
        token.setRevoked(false);

        when(refreshTokenRepository.findByToken("token")).thenReturn(Optional.of(token));

        refreshTokenService.revoke("token");

        assertTrue(token.isRevoked());
        verify(refreshTokenRepository).save(token);
    }

    @Test
    void getByToken_WhenExists_ReturnsToken() {
        RefreshToken token = new RefreshToken();
        when(refreshTokenRepository.findByToken("token")).thenReturn(Optional.of(token));

        RefreshToken result = refreshTokenService.getByToken("token");

        assertEquals(token, result);
    }

    @Test
    void getByToken_WhenNotExists_ThrowsException() {
        when(refreshTokenRepository.findByToken("token")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> refreshTokenService.getByToken("token"));
    }
}
