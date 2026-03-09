package com.andres.demotobetter.modules.users.domain.repository;

import com.andres.demotobetter.modules.users.domain.model.*;
import java.util.Optional;

public interface UserProfileRepositoryPort {
    PageResponse<UserProfile> findAll(UserProfileFilter filter, PageQuery query);
    Optional<UserProfile> findById(Long id);
    UserProfile save(UserProfile user);
    UserProfile update(UserProfile user);
}
