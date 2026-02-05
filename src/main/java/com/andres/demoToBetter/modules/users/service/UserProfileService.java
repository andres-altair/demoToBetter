package com.andres.demotobetter.modules.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.andres.demotobetter.modules.users.dto.UserProfileCreateDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileFilterDTO;
import com.andres.demotobetter.modules.users.entity.UserProfile;

public interface UserProfileService {

    UserProfileDTO save(UserProfileCreateDTO dto);

    Page<UserProfile> findAll(UserProfileFilterDTO filter, Pageable pageable);

    UserProfile findById(Long id);

    void delete(Long id);

    UserProfile update(Long id, UserProfile user);
}