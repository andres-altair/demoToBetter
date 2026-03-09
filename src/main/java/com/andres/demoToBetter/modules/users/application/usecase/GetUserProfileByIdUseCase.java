package com.andres.demotobetter.modules.users.application.usecase;

import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.domain.NotFoundException;
import com.andres.demotobetter.modules.users.application.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.application.mapper.UserProfileApplicationMapper;
import com.andres.demotobetter.modules.users.domain.model.UserProfile;
import com.andres.demotobetter.modules.users.domain.repository.UserProfileRepositoryPort;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class GetUserProfileByIdUseCase {

    private final UserProfileRepositoryPort repository;
    private final UserProfileApplicationMapper mapper;

    private static final String ERR_NOT_FOUND = "USR_404";


    public UserProfileDTO execute(Long id) {

        UserProfile domain = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Search failed: User with ID {} does not exist", id);
                    return new NotFoundException(ERR_NOT_FOUND, "User with ID " + id + " does not exist");
                });
        return mapper.toDTO(domain);
    }
}