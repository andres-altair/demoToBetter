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

// modules/security/application/usecase/AuthRefreshTokenUseCase.java
@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    @Value("${jwt.refresh}")
    private long refreshExpiration;

    private final RefreshTokenPort persistencePort;
    private final TokenServicePort tokenServicePort;

    // 1. CREATE
    public RefreshToken create(UserSecurity user) {
        String tokenStr = tokenServicePort.generateRefreshToken(user.getEmail());
        RefreshToken newToken = new RefreshToken(
                null, tokenStr, new Date(System.currentTimeMillis() + refreshExpiration), false, user);
        return persistencePort.save(newToken);
    }

    // 2. ROTATE (Mantiene la fecha de expiración original)
    public RefreshToken rotate(String oldTokenStr) {
        // Obtenemos el token actual (lanzará excepción si no existe)
        RefreshToken oldToken = getByToken(oldTokenStr);

        // 1. Revocamos el viejo (copia con revoked = true)
        RefreshToken revokedToken = new RefreshToken(
                oldToken.id(),
                oldToken.token(),
                oldToken.expiryDate(),
                true,
                oldToken.user());
        persistencePort.save(revokedToken);

        // 2. Creamos el nuevo token
        // Generamos el string del token con el TokenServicePort (JWT)
        String newTokenStr = tokenServicePort.generateRefreshToken(oldToken.user().getEmail());

        // IMPORTANTE: Pasamos oldToken.expiryDate() para mantener la misma vigencia
        RefreshToken newToken = new RefreshToken(
                null, // ID null para que la DB genere uno nuevo
                newTokenStr,
                oldToken.expiryDate(), // <--- Mantiene la fecha original
                false,
                oldToken.user());

        return persistencePort.save(newToken);
    }

    // 3. VALIDATE
    public boolean validate(String token) {
        return persistencePort.findByToken(token)
                .map(rt -> !rt.revoked() && !rt.isExpired())
                .orElse(false);
    }

    // 4. REVOKE
    public void revoke(String token) {
        persistencePort.findByToken(token).ifPresent(rt -> {
            RefreshToken revoked = new RefreshToken(
                    rt.id(), rt.token(), rt.expiryDate(), true, rt.user());
            persistencePort.save(revoked);
        });
    }

    // 5. GET BY TOKEN
    public RefreshToken getByToken(String token) {
        return persistencePort.findByToken(token)
                .orElseThrow(() -> new BadRequestException("AUTH_400", "Refresh token not found"));
    }
}
