package com.andres.demotobetter.modules.security.application.usecase;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andres.demotobetter.common.domain.BadRequestException;
import com.andres.demotobetter.modules.security.domain.model.UserSecurity;
import com.andres.demotobetter.modules.security.domain.repository.UserSecurityRepositoryPort;
import com.andres.demotobetter.modules.security.domain.service.PasswordHasherPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * Service class for registering a new user.
 * 
 * @author andres
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterSecurityUserUseCase {
    private final UserSecurityRepositoryPort repository;
    private final ResolveRolesUseCase resolveRolesUseCase; 
    private final PasswordHasherPort passwordHasher;

    private static final String ERR_BAD_REQUEST = "USR_400";


    @Transactional
    public UserSecurity execute(String email, String password, Set<String> roles) {
        if (repository.findByEmail(email).isPresent()) {
            log.warn("Failed to create user: Email {} is already in use", email);
            throw new BadRequestException(ERR_BAD_REQUEST, "Email already in use");
        }

        UserSecurity user = new UserSecurity();
        user.setUsername(email);
        user.setEmail(email);
        user.setPassword(passwordHasher.encode(password));
        user.setRoles(resolveRolesUseCase.execute(roles));
        
        log.info("Credentials for security created successfully for user: {}", email);
        return repository.save(user);
    }
}

