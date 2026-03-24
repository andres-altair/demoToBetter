package com.andres.demotobetter.modules.security.infrastructure.persistence.adapter;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.andres.demotobetter.modules.users.domain.repository.UserSecurityManagementPort;
import com.andres.demotobetter.modules.security.application.usecase.RegisterSecurityUserUseCase;
import com.andres.demotobetter.modules.security.application.usecase.DisableUserSecurityUseCase;

import lombok.RequiredArgsConstructor;
/**
 * Adapter for UserSecurityManagementPort
 * 
 * @author andres
 */
@Component
@RequiredArgsConstructor
public class UserSecurityManagementAdapter implements UserSecurityManagementPort {

    private final RegisterSecurityUserUseCase registerUseCase;
    private final DisableUserSecurityUseCase disableUseCase;

    @Override
    public Long createSecurityUser(String email, String password, Set<String> roles) {
        return registerUseCase.execute(email, password, roles).getId();
    }

    @Override
    public void disableUser(Long securityUserId) {
        disableUseCase.execute(securityUserId);
    }
}
