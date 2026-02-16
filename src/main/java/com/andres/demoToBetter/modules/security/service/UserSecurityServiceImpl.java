package com.andres.demotobetter.modules.security.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.common.exception.custom.NotFoundException;
import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class that implements UserSecurityService.
 * 
 * @author andres
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserSecurityServiceImpl implements UserSecurityService {
    private static final String ERR_BAD_REQUEST = "ERR_BAD_REQUEST";
    private static final String ERR_NOT_FOUND = "USR_404";

    private final UserSecurityRepository userSecurityRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserSecurity createSecurityUser(String email, String password, Set<String> roles) {
        log.debug("Verificando disponibilidad de email para nuevo usuario: {}", email);

        if (userSecurityRepository.findByEmail(email).isPresent()) {
            log.warn("Fallo al crear usuario: El email {} ya está registrado", email);
            throw new BadRequestException(ERR_BAD_REQUEST, "Email already in use");
        }
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUsername(email);
        userSecurity.setEmail(email);
        userSecurity.setPassword(passwordEncoder.encode(password));
        userSecurity.setRoles(roleService.resolveRoles(roles));
        log.info("Credenciales de seguridad creadas exitosamente para el usuario: {}", email);
        return userSecurityRepository.save(userSecurity);
    }

    @SuppressWarnings("null")
    @Override
    public void disableUser(Long id) {
        log.info("Solicitud de desactivación para el usuario de seguridad con ID: {}", id);

        UserSecurity user = userSecurityRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo desactivar: Usuario de seguridad con ID {} no existe", id);
                    return new NotFoundException(ERR_NOT_FOUND, "User with ID " + id + " does not exist");
                });

        user.setActive(false);
        userSecurityRepository.save(user);
        log.info("Estado del usuario ID: {} cambiado a INACTIVO", id);
    }
}