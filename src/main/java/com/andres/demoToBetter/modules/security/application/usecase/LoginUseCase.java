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
 * Login use case
 * 
 * @author andres
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoginUseCase {

    private static final String ERR_BAD_REQUEST = "AUTH_400";

    private final IdentityManagerPort identityManager;
    private final TokenServicePort tokenService;
    private final UserSecurityRepositoryPort userRepository;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public ResponseLoginTokenDTO execute(LoginDTO loginDTO) {
        log.info("Starting authentication process for: {}", loginDTO.getEmail());

        identityManager.authenticate(loginDTO.getEmail(), loginDTO.getPassword());

        UserSecurity user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failure: User {} not found in repository", loginDTO.getEmail());
                    return new BadRequestException(ERR_BAD_REQUEST, "User not found");
                });

        String accessToken = tokenService.generateToken(user.getEmail());

        RefreshToken refreshToken = refreshTokenUseCase.create(user);

        log.info("Successful authentication for: {}. Token generated.", loginDTO.getEmail());
        return new ResponseLoginTokenDTO(accessToken, refreshToken.token());
    }
}
