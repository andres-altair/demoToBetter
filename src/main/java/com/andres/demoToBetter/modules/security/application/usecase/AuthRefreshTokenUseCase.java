package com.andres.demotobetter.modules.security.application.usecase;

import com.andres.demotobetter.common.domain.BadRequestException;
import com.andres.demotobetter.modules.security.application.dto.ResponseLoginTokenDTO;
import com.andres.demotobetter.modules.security.domain.model.RefreshToken;
import com.andres.demotobetter.modules.security.domain.service.TokenServicePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthRefreshTokenUseCase {
    
    private final TokenServicePort tokenService;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public ResponseLoginTokenDTO execute (String tokenStr) {
        log.info("Iniciando proceso de refresco de token");

        if (!refreshTokenUseCase.validate(tokenStr)) {
            throw new BadRequestException("AUTH_401", "Invalid or expired refresh token");
        }

        String username = tokenService.extractUsername(tokenStr);
        RefreshToken oldToken = refreshTokenUseCase.getByToken(tokenStr);

        if (!oldToken.user().getEmail().equals(username)) {
            throw new BadRequestException("AUTH_403", "Token does not belong to this user");
        }

        if (!oldToken.user().isActive()) {
            throw new BadRequestException("AUTH_403", "User account is disabled");
        }

        RefreshToken newToken = refreshTokenUseCase.rotate(tokenStr);
        String newAccessToken = tokenService.generateToken(username);

        return new ResponseLoginTokenDTO(newAccessToken, newToken.token());
    }

}
