package com.andres.demotobetter.modules.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andres.demotobetter.modules.security.dto.LoginDTO;
import com.andres.demotobetter.modules.security.dto.TokenDTO;
import com.andres.demotobetter.modules.security.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        
        return authService.login(loginDTO);
    }
    


}
