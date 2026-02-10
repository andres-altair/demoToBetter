package com.andres.demotobetter.modules.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.ResponseLoginTokenDTO;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_WhenValidCredentials_ReturnToken() {
        LoginDTO loginDTO = new LoginDTO("andres@mail.com", "password");
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsServiceImpl.loadUserByUsername(loginDTO.getEmail()))
                .thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");
        ResponseLoginTokenDTO responseLoginTokenDTO = authService.login(loginDTO);

        assertEquals("jwt-token", responseLoginTokenDTO.getToken());
        verify(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        verify(userDetailsServiceImpl).loadUserByUsername(loginDTO.getEmail());
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void login_WhenInvalidCredentials_ThrowException() {
        LoginDTO loginDTO = new LoginDTO("andres@mail.com", "password");
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginDTO);
        });
        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(userDetailsServiceImpl);
        verifyNoInteractions(jwtService);
    }
}