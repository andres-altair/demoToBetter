package com.andres.demotobetter.modules.security.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.andres.demotobetter.modules.security.domain.service.IdentityManagerPort;

@Component
@RequiredArgsConstructor
public class SpringIdentityAdapter implements IdentityManagerPort {

    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticate(String email, String password) {
        // Aquí ocurre el acoplamiento técnico, pero está encapsulado en Infraestructura
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}
