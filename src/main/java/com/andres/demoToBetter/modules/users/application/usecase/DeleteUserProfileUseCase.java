package com.andres.demotobetter.modules.users.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andres.demotobetter.common.domain.NotFoundException;
import com.andres.demotobetter.modules.users.domain.repository.UserSecurityManagementPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * Service class for deactivating a user.   
 * 
 * @author andres
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUserProfileUseCase {

    private final UserSecurityManagementPort securityPort;

    private static final String ERR_NOT_FOUND = "USR_404";

    @Transactional
    public void execute(Long id) {
        log.info("Deactivating user with ID: {}", id);

        if (id == null) {
            log.warn("Deactivation failed: ID cannot be null");
            throw new NotFoundException(ERR_NOT_FOUND, "ID cannot be null");
        }

        securityPort.disableUser(id);

        log.info("User ID: {} marked as inactive", id);
    }
}

