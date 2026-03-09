package com.andres.demotobetter.modules.security.application.usecase;

import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.domain.NotFoundException;
import com.andres.demotobetter.modules.security.domain.model.UserSecurity;
import com.andres.demotobetter.modules.security.domain.repository.UserSecurityRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisableUserSecurityUseCase {

    private final UserSecurityRepositoryPort repository;
    private static final String ERR_NOT_FOUND = "USR_404";

    public void execute(Long id) {
        log.info("Deactivation request for security user with ID: {}", id);

        UserSecurity user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Deactivation failed: User with ID {} does not exist", id);
                    return new NotFoundException(ERR_NOT_FOUND, "User with ID " + id + " does not exist");
                });

        user.setActive(false);

        repository.save(user);
        log.info("User ID status: {} changed to FALSE", id);
    }
}

