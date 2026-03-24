package com.andres.demotobetter.modules.users.application.usecase;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.andres.demotobetter.modules.users.application.dto.UserProfileCreateDTO;
import com.andres.demotobetter.modules.users.application.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.application.mapper.UserProfileApplicationMapper;
import com.andres.demotobetter.modules.users.domain.model.UserProfile;
import com.andres.demotobetter.modules.users.domain.repository.UserProfileRepositoryPort;
import com.andres.demotobetter.modules.users.domain.repository.UserSecurityManagementPort;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * Service class for creating a new user.
 * 
 * @author andres
 */
@Service
@AllArgsConstructor
@Slf4j
public class CreateUserProfileUseCase {

    private final UserProfileRepositoryPort repository; 
    private final UserSecurityManagementPort securityPort;
    private final UserProfileApplicationMapper mapper; 

    @Transactional
    public UserProfileDTO execute(UserProfileCreateDTO dto) {
        log.info("Creating new user: {}", dto.getEmail());

        Long securityId = securityPort.createSecurityUser(dto.getEmail(), dto.getPassword(), dto.getRoles());

        UserProfile profile = mapper.toDomain(dto);
        profile.setSecurityId(securityId);

        UserProfile savedProfile = repository.save(profile);

        log.info("User saved successfully with ID: {}", savedProfile.getId());
        
        return mapper.toDTO(savedProfile);
    }
}

