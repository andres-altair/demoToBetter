package com.andres.demotobetter.modules.security.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.andres.demotobetter.modules.security.domain.service.IdentityManagerPort;
/**
 * Adapter class for Spring's AuthenticationManager.
 * 
 * @author andres
 */
@Component
@RequiredArgsConstructor
public class SpringIdentityAdapter implements IdentityManagerPort {

    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}
