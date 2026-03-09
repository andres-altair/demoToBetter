// package com.andres.demotobetter.modules.security.service.jwt;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// import java.util.Date;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.test.util.ReflectionTestUtils;

// import com.andres.demotobetter.common.domain.BadRequestException;
// import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.RefreshTokenEntity;
// import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.UserSecurityEntity;
// import com.andres.demotobetter.modules.security.infrastructure.persistence.repository.RefreshTokenJpaRepository;
// import com.andres.demotobetter.modules.security.service.jwt.JwtService;
// import com.andres.demotobetter.modules.security.service.jwt.RefreshTokenServiceImpl;

// @ExtendWith(MockitoExtension.class)
// class RefreshTokenServiceImplTest {
//     @Mock
//     private RefreshTokenJpaRepository refreshTokenRepository;
//     @Mock
//     private JwtService jwtService;

//     @InjectMocks
//     private RefreshTokenServiceImpl refreshTokenService;

//     private final long refreshExpiration = 604800000;
//     private UserSecurityEntity user;

//     @SuppressWarnings("null")
//     @BeforeEach
//     void setUp() {
//         ReflectionTestUtils.setField(refreshTokenService, "refreshExpiration", refreshExpiration);
//         user = new UserSecurityEntity();
//         user.setEmail("andres@mail.com");
//     }

//     @SuppressWarnings("null")
//     @Test
//     void create_WhenUserValid_ReturnsToken() {
//         RefreshTokenEntity saved = new RefreshTokenEntity();
//         saved.setToken("refresh-token");
//         saved.setUser(user);
//         saved.setExpiryDate(new Date(System.currentTimeMillis() + refreshExpiration));

//         when(jwtService.generateRefreshToken(user.getEmail())).thenReturn("refresh-token");
//         when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(saved);

//         RefreshTokenEntity result = refreshTokenService.create(user);

//         assertEquals("refresh-token", result.getToken());
//         assertEquals(user, result.getUser());
//         assertNotNull(result.getExpiryDate());
//         verify(jwtService).generateRefreshToken(user.getEmail());
//         verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
//     }

//     @Test
//     void validate_WhenTokenIsValid_ReturnsTrue() {
//         String tokenStr = "valid-token";
//         RefreshTokenEntity rt = new RefreshTokenEntity();
//         rt.setRevoked(false);
//         rt.setExpiryDate(new Date(System.currentTimeMillis() + 10000)); // Futuro

//         when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(rt));

//         boolean isValid = refreshTokenService.validate(tokenStr);

//         assertTrue(isValid);
//     }

//     @Test
//     void validate_WhenTokenIsRevoked_ReturnsFalse() {
//         String tokenStr = "revoked-token";
//         RefreshTokenEntity rt = new RefreshTokenEntity();
//         rt.setRevoked(true);
//         rt.setExpiryDate(new Date(System.currentTimeMillis() + 10000));

//         when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(rt));

//         boolean isValid = refreshTokenService.validate(tokenStr);

//         assertFalse(isValid);
//     }

//     @Test
//     void validate_WhenTokenExpired_ReturnsFalse() {
//         String tokenStr = "revoked-token";
//         RefreshTokenEntity rt = new RefreshTokenEntity();
//         rt.setRevoked(false);
//         rt.setExpiryDate(new Date(System.currentTimeMillis() - 10000));

//         when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(rt));

//         boolean isValid = refreshTokenService.validate(tokenStr);

//         assertFalse(isValid);
//     }

//     @SuppressWarnings("null")
//     @Test
//     void rotate_WhenValidOldToken_ReturnsNewToken() {
//         RefreshTokenEntity oldToken = new RefreshTokenEntity();
//         oldToken.setUser(user);
//         oldToken.setRevoked(false);
//         oldToken.setExpiryDate(new Date(System.currentTimeMillis() + 10000));
//         RefreshTokenEntity newToken = new RefreshTokenEntity();
//         newToken.setToken("new-refresh-token");
//         newToken.setUser(user);

//         when(jwtService.generateRefreshToken(user.getEmail())).thenReturn("new-refresh-token");
//         when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(newToken);

//         RefreshTokenEntity result = refreshTokenService.rotate(oldToken);

//         assertEquals("new-refresh-token", result.getToken());
//         assertEquals(user, result.getUser());
//         assertFalse(result.isRevoked());
//         verify(refreshTokenRepository, times(2)).save(any(RefreshTokenEntity.class));
//     }

//     @Test
//     void revoke_WhenTokenExists_SetsRevokedTrue() {
//         RefreshTokenEntity token = new RefreshTokenEntity();
//         token.setRevoked(false);

//         when(refreshTokenRepository.findByToken("token")).thenReturn(Optional.of(token));

//         refreshTokenService.revoke("token");

//         assertTrue(token.isRevoked());
//         verify(refreshTokenRepository).save(token);
//     }

//     @Test
//     void getByToken_WhenExists_ReturnsToken() {
//         RefreshTokenEntity token = new RefreshTokenEntity();
//         when(refreshTokenRepository.findByToken("token")).thenReturn(Optional.of(token));

//         RefreshTokenEntity result = refreshTokenService.getByToken("token");

//         assertEquals(token, result);
//     }

//     @Test
//     void getByToken_WhenNotExists_ThrowsException() {
//         when(refreshTokenRepository.findByToken("token")).thenReturn(Optional.empty());

//         assertThrows(BadRequestException.class, () -> refreshTokenService.getByToken("token"));
//     }
// }
