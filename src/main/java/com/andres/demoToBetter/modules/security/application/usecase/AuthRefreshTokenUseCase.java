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

    
    private static final String ERR_BAD_REQUEST = "AUTH_400";

    public ResponseLoginTokenDTO execute (String tokenStr) {
        log.info("Starting token refresh process");

        if (!refreshTokenUseCase.validate(tokenStr)) {
            log.warn("Refresh attempt failed: Invalid or expired token");
            throw new BadRequestException(ERR_BAD_REQUEST, "Invalid or expired refresh token");
        }

        String username = tokenService.extractUsername(tokenStr);
        RefreshToken oldToken = refreshTokenUseCase.getByToken(tokenStr);

        if (!oldToken.user().getEmail().equals(username)) {
            log.warn("The token does not belong to the user {}", username);
            throw new BadRequestException(ERR_BAD_REQUEST, "Token does not belong to this user");
        }

        if (!oldToken.user().isActive()) {
            log.warn("Refreshment denied: The user account {} is disabled", username);
            throw new BadRequestException(ERR_BAD_REQUEST, "User account is disabled");
        }

        RefreshToken newToken = refreshTokenUseCase.rotate(tokenStr);
        String newAccessToken = tokenService.generateToken(username);

        log.info("Token successfully refreshed for the user: {}", username);
        return new ResponseLoginTokenDTO(newAccessToken, newToken.token());
    }

}
