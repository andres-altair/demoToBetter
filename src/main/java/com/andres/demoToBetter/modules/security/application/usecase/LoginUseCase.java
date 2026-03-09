package com.andres.demotobetter.modules.security.application.usecase;

import com.andres.demotobetter.common.domain.BadRequestException;
import com.andres.demotobetter.modules.security.application.dto.LoginDTO;
import com.andres.demotobetter.modules.security.application.dto.ResponseLoginTokenDTO;
import com.andres.demotobetter.modules.security.domain.model.RefreshToken;
import com.andres.demotobetter.modules.security.domain.model.UserSecurity;
import com.andres.demotobetter.modules.security.domain.repository.UserSecurityRepositoryPort;
import com.andres.demotobetter.modules.security.domain.service.IdentityManagerPort;
import com.andres.demotobetter.modules.security.domain.service.TokenServicePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso principal para la autenticación (Login y Refresh).
 * Ahora 100% Hexagonal: sin dependencias de Spring Security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoginUseCase {

    private static final String ERR_BAD_REQUEST = "AUTH_400";

    private final IdentityManagerPort identityManager; // Abstracción de autenticación
    private final TokenServicePort tokenService;
    private final UserSecurityRepositoryPort userRepository;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public ResponseLoginTokenDTO execute(LoginDTO loginDTO) {
        log.info("Iniciando proceso de autenticación para: {}", loginDTO.getEmail());

        // 1. Autenticación a través del puerto (la implementación técnica está en Infrastructure)
        identityManager.authenticate(loginDTO.getEmail(), loginDTO.getPassword());

        // 2. Obtener usuario del dominio
        UserSecurity user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failure: User {} not found in repository", loginDTO.getEmail());
                    return new BadRequestException(ERR_BAD_REQUEST, "User not found");
                });

        // 3. Generar Access Token
        String accessToken = tokenService.generateToken(user.getEmail());

        // 4. Crear Refresh Token
        RefreshToken refreshToken = refreshTokenUseCase.create(user);

        log.info("Autenticación exitosa para: {}", user.getEmail());
        return new ResponseLoginTokenDTO(accessToken, refreshToken.token());
    }
}
