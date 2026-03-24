package com.andres.demotobetter.modules.users.domain.repository;

import com.andres.demotobetter.modules.users.domain.model.*;
import java.util.Optional;
/**
 * Interface for UserProfile repository operations.
 * 
 * @author andres
 */
public interface UserProfileRepositoryPort {
    PageResponse<UserProfile> findAll(UserProfileFilter filter, PageQuery query);
    Optional<UserProfile> findById(Long id);
    UserProfile save(UserProfile user);
    UserProfile update(UserProfile user);
}
