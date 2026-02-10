package com.andres.demotobetter.modules.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.common.exception.custom.NotFoundException;
import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.ResponseLoginTokenDTO;
import com.andres.demotobetter.modules.security.entity.RefreshToken;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;

import lombok.RequiredArgsConstructor;

/**
 * Class that manages the authentication process.
 * @author andres
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String ERR_BAD_REQUEST = "AUTH_400";
    private static final String ERR_NOT_FOUND = "AUTH_404";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final UserSecurityRepository userSecurityRepository;

    public ResponseLoginTokenDTO login(LoginDTO loginDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()));

        var userDetails = userDetailsServiceImpl.loadUserByUsername(loginDTO.getEmail());
        var userSecurity = userSecurityRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(ERR_NOT_FOUND, "User not found"));

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.create(userSecurity);

        return new ResponseLoginTokenDTO(accessToken, refreshToken.getToken());
    }

    public ResponseLoginTokenDTO refresh(String refreshToken) {

        if (!refreshTokenService.validate(refreshToken)) {
            throw new BadRequestException(ERR_BAD_REQUEST, "Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);

        var userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        RefreshToken oldToken = refreshTokenService.getByToken(refreshToken);
        
        if (!oldToken.getUser().getEmail().equals(username)) {
            throw new BadRequestException(ERR_BAD_REQUEST, "Token does not belong to this user");
        }
        RefreshToken newToken = refreshTokenService.rotate(oldToken);

        String newAccessToken = jwtService.generateToken(userDetails);

        return new ResponseLoginTokenDTO(newAccessToken, newToken.getToken());
    }
}