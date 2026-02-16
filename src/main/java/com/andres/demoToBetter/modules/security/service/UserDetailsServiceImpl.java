package com.andres.demotobetter.modules.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andres.demotobetter.modules.security.model.UserDetailsImpl;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class that implements UserDetailsService.
 * 
 * @author andres
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserSecurityRepository userSecurityRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("Cargando detalles de seguridad para el usuario: {}", email);
        var user = userSecurityRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Carga fallida: No se encontró usuario con el email: {}", email);
                    return new UsernameNotFoundException("User not found");
                });

        log.debug("Usuario {} cargado correctamente desde la base de datos", email);
        return new UserDetailsImpl(user);
    }
}