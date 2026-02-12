package com.andres.demotobetter.modules.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.ResponseLoginTokenDTO;
import com.andres.demotobetter.modules.security.entity.RefreshToken;
import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;
import com.andres.demotobetter.modules.security.service.jwt.JwtService;
import com.andres.demotobetter.modules.security.service.jwt.RefreshTokenService;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private UserSecurityRepository userSecurityRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_WhenValidCredentials_ReturnToken() {
        LoginDTO loginDTO = new LoginDTO("andres@mail.com", "password");
        UserDetails userDetails = mock(UserDetails.class);
        UserSecurity userSecurity = new UserSecurity();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");

        when(userDetailsServiceImpl.loadUserByUsername(loginDTO.getEmail())).thenReturn(userDetails);
        when(userSecurityRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(userSecurity));
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");
        when(refreshTokenService.create(userSecurity)).thenReturn(refreshToken);

        ResponseLoginTokenDTO response = authService.login(loginDTO);

        assertEquals("jwt-token", response.getAcsessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userDetailsServiceImpl).loadUserByUsername(loginDTO.getEmail());
        verify(jwtService).generateToken(userDetails);
        verify(refreshTokenService).create(userSecurity);
        verifyNoMoreInteractions(userDetailsServiceImpl, jwtService, refreshTokenService, userSecurityRepository);
    }

    @Test
    void login_ShouldThrowBadRequestException_WhenUserDoesNotExistInRepo() {
        LoginDTO loginDTO = new LoginDTO("missing@mail.com", "password");

        when(userDetailsServiceImpl.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));
        when(userSecurityRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> {
            authService.login(loginDTO);
        });

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userSecurityRepository).findByEmail(loginDTO.getEmail());
        verifyNoInteractions(jwtService, refreshTokenService);
    }

    @Test
    void login_WhenInvalidCredentials_ThrowException() {
        LoginDTO loginDTO = new LoginDTO("andres@mail.com", "password");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginDTO));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsServiceImpl, jwtService, refreshTokenService, userSecurityRepository);
    }

    @Test
    void refresh_WhenValidRefreshToken_ReturnToken() {
        String oldTokenString = "old-token";
        String username = "andres@mail.com";
        UserDetails userDetails = mock(UserDetails.class);
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setEmail(username);
        RefreshToken dbToken = new RefreshToken();
        dbToken.setToken(oldTokenString);
        dbToken.setUser(userSecurity);
        RefreshToken newToken = new RefreshToken();
        newToken.setToken("new-refresh-token");

        when(refreshTokenService.validate(oldTokenString)).thenReturn(true);
        when(jwtService.extractUsername(oldTokenString)).thenReturn(username);
        when(userDetailsServiceImpl.loadUserByUsername(username)).thenReturn(userDetails);
        when(refreshTokenService.getByToken(oldTokenString)).thenReturn(dbToken);
        when(refreshTokenService.rotate(dbToken)).thenReturn(newToken);
        when(jwtService.generateToken(userDetails)).thenReturn("new-access-token");

        ResponseLoginTokenDTO response = authService.refresh(oldTokenString);

        assertEquals("new-access-token", response.getAcsessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
        verify(refreshTokenService).validate(oldTokenString);
        verify(jwtService).extractUsername(oldTokenString);
        verify(userDetailsServiceImpl).loadUserByUsername(username);
        verify(refreshTokenService).getByToken(oldTokenString);
        verify(refreshTokenService).rotate(dbToken);
        verify(jwtService).generateToken(userDetails);
        verifyNoMoreInteractions(refreshTokenService, jwtService, userDetailsServiceImpl);
    }

    @Test
    void refresh_WhenInvalidRefreshToken_ThrowException() {
        when(refreshTokenService.validate("Invalid token")).thenReturn(false);
        assertThrows(BadRequestException.class, () -> {
            authService.refresh("Invalid token");
        });
        verify(refreshTokenService).validate(anyString());
        verifyNoMoreInteractions(refreshTokenService, jwtService, userDetailsServiceImpl);
    }

    @Test
    void refresh_WhenTokenDoesNotBelongToUser_ThrowsBadRequest() {
        String token = "token";
        String username = "andres@mail.com";
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setEmail(username);
        RefreshToken dbToken = new RefreshToken();
        dbToken.setUser(userSecurity);

        when(refreshTokenService.validate(token)).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn("correct@example.com");
        when(refreshTokenService.getByToken(token)).thenReturn(dbToken);

        assertThrows(BadRequestException.class, () -> authService.refresh(token));

        verify(refreshTokenService).validate(token);
        verify(jwtService).extractUsername(token);
        verify(refreshTokenService).getByToken(token);
        verifyNoMoreInteractions(refreshTokenService, jwtService);
    }

    @Test
    void refresh_WhenUserIsInactive_ThrowException() {
        String token = "token";
        String username = "andres@mail.com";
        UserSecurity user = new UserSecurity();
        user.setEmail(username);
        user.setActive(false);
        RefreshToken dbToken = new RefreshToken();
        dbToken.setToken(token);
        dbToken.setUser(user);

        when(refreshTokenService.validate(token)).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn(username);
        // when(userDetailsServiceImpl.loadUserByUsername(username)).thenReturn(mock(UserDetails.class));
        when(refreshTokenService.getByToken(token)).thenReturn(dbToken);

        assertThrows(BadRequestException.class, () -> authService.refresh(token));

        verify(refreshTokenService).validate(token);
        verify(jwtService).extractUsername(token);
        verify(refreshTokenService).getByToken(token);
        verifyNoMoreInteractions(refreshTokenService, jwtService);
    }
}