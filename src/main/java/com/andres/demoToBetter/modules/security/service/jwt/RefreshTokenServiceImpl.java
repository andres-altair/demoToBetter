package com.andres.demotobetter.modules.security.service.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.entity.RefreshToken;
import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
/**
 * Class that implements RefreshTokenService.
 * @author andres
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh}")
    private long refreshExpiration;

    private static final String ERR_BAD_REQUEST = "AUTH_400";

    private final RefreshTokenRepository repository;
    private final JwtService jwtService;

    @Override
    public RefreshToken create(UserSecurity user) {
        String token = jwtService.generateRefreshToken(user.getEmail());
        RefreshToken refresh = new RefreshToken();
        refresh.setToken(token);
        refresh.setUser(user);
        refresh.setExpiryDate(new Date(System.currentTimeMillis() + refreshExpiration));

        return repository.save(refresh);
    }

    @Override
    public boolean validate(String token) {
        return repository.findByToken(token).filter(rt -> !rt.isRevoked())
                .filter(rt -> rt.getExpiryDate().after(new Date())).isPresent();
    }

    @Override
    public RefreshToken rotate(RefreshToken oldToken) {
        oldToken.setRevoked(true);
        repository.save(oldToken);

        RefreshToken newToken = new RefreshToken();
        newToken.setToken(jwtService.generateRefreshToken(oldToken.getUser().getEmail()));
        newToken.setUser(oldToken.getUser());
        newToken.setExpiryDate(oldToken.getExpiryDate());
        return repository.save(newToken);
    }
    @Override
    public void revoke(String token) {
        repository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            repository.save(rt);
        });
    }

    @Override
    public RefreshToken getByToken(String token) {
        return repository.findByToken(token)
                .orElseThrow(() -> new BadRequestException(ERR_BAD_REQUEST, "Refresh token not found"));
    }
}