package com.andres.demotobetter.modules.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.ResponseLoginTokenDTO;
import com.andres.demotobetter.modules.security.service.AuthServiceImpl;

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
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseLoginTokenDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("Intento de login para el usuario: {}", loginDTO.getEmail());
        return authService.login(loginDTO);
    }

    @PostMapping("/refresh")
    public ResponseLoginTokenDTO refresh(@RequestBody Map<String, String> body) {
        log.info("Petición de refresco de token recibida");
        return authService.refresh(body.get("refreshToken"));
    }
}