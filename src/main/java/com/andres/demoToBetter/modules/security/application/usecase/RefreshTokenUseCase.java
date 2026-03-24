package com.andres.demotobetter.modules.security.application.usecase;

import com.andres.demotobetter.common.domain.BadRequestException;
import com.andres.demotobetter.modules.security.domain.model.RefreshToken;
import com.andres.demotobetter.modules.security.domain.model.UserSecurity;
import com.andres.demotobetter.modules.security.domain.repository.RefreshTokenPort;
import com.andres.demotobetter.modules.security.domain.service.TokenServicePort;

import lombok.RequiredArgsConstructor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for RefreshToken operations.
 * 
 * @author andres
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    @Value("${jwt.refresh}")
    private long refreshExpiration;

    private final RefreshTokenPort persistencePort;
    private final TokenServicePort tokenServicePort;

    public RefreshToken create(UserSecurity user) {
        String tokenStr = tokenServicePort.generateRefreshToken(user.getEmail());
        RefreshToken newToken = new RefreshToken(
                null, tokenStr, new Date(System.currentTimeMillis() + refreshExpiration), false, user);
        return persistencePort.save(newToken);
    }

    public RefreshToken rotate(String oldTokenStr) {
        RefreshToken oldToken = getByToken(oldTokenStr);

        RefreshToken revokedToken = new RefreshToken(
                oldToken.id(),
                oldToken.token(),
                oldToken.expiryDate(),
                true,
                oldToken.user());
        persistencePort.save(revokedToken);

        String newTokenStr = tokenServicePort.generateRefreshToken(oldToken.user().getEmail());

        RefreshToken newToken = new RefreshToken(
                null, 
                newTokenStr,
                oldToken.expiryDate(),
                false,
                oldToken.user());

        return persistencePort.save(newToken);
    }

    public boolean validate(String token) {
        return persistencePort.findByToken(token)
                .map(rt -> !rt.revoked() && !rt.isExpired())
                .orElse(false);
    }

    public void revoke(String token) {
        persistencePort.findByToken(token).ifPresent(rt -> {
            RefreshToken revoked = new RefreshToken(
                    rt.id(), rt.token(), rt.expiryDate(), true, rt.user());
            persistencePort.save(revoked);
        });
    }

    public RefreshToken getByToken(String token) {
        return persistencePort.findByToken(token)
                .orElseThrow(() -> new BadRequestException("AUTH_400", "Refresh token not found"));
    }
}
