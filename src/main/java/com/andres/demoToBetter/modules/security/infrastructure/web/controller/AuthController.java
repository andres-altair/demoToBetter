package com.andres.demotobetter.modules.security.infrastructure.web.controller;

import com.andres.demotobetter.modules.security.application.dto.LoginDTO;
import com.andres.demotobetter.modules.security.application.dto.ResponseLoginTokenDTO;
import com.andres.demotobetter.modules.security.application.usecase.AuthRefreshTokenUseCase;
import com.andres.demotobetter.modules.security.application.usecase.LoginUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller that handles authentication requests.
 * @author andres
 */
@Tag(name = "Authentication Controller", description = "Endpoints for authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    // Inyectamos Casos de Uso, no Servicios
    private final LoginUseCase loginUseCase;
    private final AuthRefreshTokenUseCase refreshTokenUseCase;

    @Operation(summary = "Login", description = "Endpoint for user login")
    @PostMapping("/login")
    public ResponseLoginTokenDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("User login attempt: {}", loginDTO.getEmail());
        // Ejecuta la lógica de autenticación e identidad
        return loginUseCase.execute(loginDTO);
    }

    @Operation(summary = "Refresh Token", description = "Endpoint for token refresh")
    @PostMapping("/refresh")
    public ResponseLoginTokenDTO refresh(@RequestBody Map<String, String> body) {
        String tokenStr = body.get("refreshToken");
        log.info("Token refresh request received");
        // Ejecuta la lógica de validación y rotación de tokens
        return refreshTokenUseCase.execute(tokenStr);
    }
}
