package com.andres.demotobetter.modules.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.ResponseLoginTokenDTO;

import lombok.RequiredArgsConstructor;

/**
 * Class that manages the authentication process.
 * @author andres
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public ResponseLoginTokenDTO login(LoginDTO loginDTO) {

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        var user = userDetailsServiceImpl.loadUserByUsername(loginDTO.getEmail());
        String token = jwtService.generateToken(user);
        return new ResponseLoginTokenDTO(token);
    }
}