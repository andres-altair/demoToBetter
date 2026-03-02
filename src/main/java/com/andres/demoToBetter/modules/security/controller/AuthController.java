package com.andres.demotobetter.modules.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.ResponseLoginTokenDTO;
import com.andres.demotobetter.modules.security.service.AuthServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    private final AuthServiceImpl authService;

    @Operation(summary = "Login", description = "Endpoint for user login")
    @PostMapping("/login")
    public ResponseLoginTokenDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("User login attempt: {}", loginDTO.getEmail());
        return authService.login(loginDTO);
    }

    @Operation(summary = "Refresh Token", description = "Endpoint for token refresh")
    @PostMapping("/refresh")
    public ResponseLoginTokenDTO refresh(@RequestBody Map<String, String> body) {
        log.info("Token refresh request received");
        return authService.refresh(body.get("refreshToken"));
    }
}