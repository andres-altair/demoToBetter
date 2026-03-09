package com.andres.demotobetter.modules.users.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andres.demotobetter.common.domain.BadRequestException;
import com.andres.demotobetter.common.domain.NotFoundException;
import com.andres.demotobetter.modules.users.application.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.application.dto.UserProfileUpdateDTO;
import com.andres.demotobetter.modules.users.application.mapper.UserProfileApplicationMapper;
import com.andres.demotobetter.modules.users.domain.model.UserProfile;
import com.andres.demotobetter.modules.users.domain.repository.UserProfileRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserProfileUseCase {

    private final UserProfileRepositoryPort repository;
    private final UserProfileApplicationMapper mapper; 

    @Transactional
    public UserProfileDTO execute(Long id, UserProfileUpdateDTO dto) {
        log.info("Starting profile update ID: {}", id);

        if (id == null) {
            throw new BadRequestException("USR_400", "ID cannot be null");
        }

        UserProfile existingProfile = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed: User with ID {} does not exist", id);
                    return new NotFoundException("USR_404", "User not found");
                });

        mapper.updateDomainFromDto(existingProfile, dto);

        UserProfile updatedProfile = repository.update(existingProfile);

        log.info("Profile ID: {} successfully updated", id);
        
        return mapper.toDTO(updatedProfile);
    }
}

