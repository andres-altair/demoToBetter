package com.andres.demotobetter.modules.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.ResponseLoginTokenDTO;
import com.andres.demotobetter.modules.security.entity.RefreshToken;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;
import com.andres.demotobetter.modules.security.service.jwt.JwtService;
import com.andres.demotobetter.modules.security.service.jwt.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class that implements AuthService.
 * 
 * @author andres
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private static final String ERR_BAD_REQUEST = "AUTH_400";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final UserSecurityRepository userSecurityRepository;

    @Override
    public ResponseLoginTokenDTO login(LoginDTO loginDTO) {
        log.info("Iniciando proceso de autenticación para: {}", loginDTO.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()));

        var userDetails = userDetailsServiceImpl.loadUserByUsername(loginDTO.getEmail());
        var userSecurity = userSecurityRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    log.warn("Fallo de login: Usuario {} no encontrado en repositorio", loginDTO.getEmail());
                    return new BadRequestException(ERR_BAD_REQUEST, "User not found");
                });

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.create(userSecurity);

        log.info("Autenticación exitosa para: {}. Token generado.", loginDTO.getEmail());
        return new ResponseLoginTokenDTO(accessToken, refreshToken.getToken());
    }

    @Override
    public ResponseLoginTokenDTO refresh(String refreshToken) {
        log.info("Iniciando rotación de Refresh Token");

        if (!refreshTokenService.validate(refreshToken)) {
            log.warn("Intento de refresco fallido: Token inválido o expirado");
            throw new BadRequestException(ERR_BAD_REQUEST, "Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        RefreshToken oldToken = refreshTokenService.getByToken(refreshToken);

        if (!oldToken.getUser().getEmail().equals(username)) {
            log.error("VIOLACIÓN DE SEGURIDAD: El token no pertenece al usuario {}", username);
            throw new BadRequestException(ERR_BAD_REQUEST, "Token does not belong to this user");
        }

        if (!oldToken.getUser().isActive()) {
            log.warn("Refresco denegado: La cuenta del usuario {} está desactivada", username);
            throw new BadRequestException(ERR_BAD_REQUEST, "User account is disabled");
        }
        RefreshToken newToken = refreshTokenService.rotate(oldToken);

        String newAccessToken = jwtService.generateToken(userDetails);

        log.info("Token refrescado exitosamente para el usuario: {}", username);
        return new ResponseLoginTokenDTO(newAccessToken, newToken.getToken());
    }
}