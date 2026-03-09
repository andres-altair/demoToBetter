package com.andres.demotobetter.modules.users.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.domain.BadRequestException;
import com.andres.demotobetter.modules.users.application.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.application.mapper.UserProfileApplicationMapper;
import com.andres.demotobetter.modules.users.domain.model.PageQuery;
import com.andres.demotobetter.modules.users.domain.model.PageResponse;
import com.andres.demotobetter.modules.users.domain.model.UserProfile;
import com.andres.demotobetter.modules.users.domain.model.UserProfileFilter;
import com.andres.demotobetter.modules.users.domain.repository.UserProfileRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCase {
    
    private final UserProfileRepositoryPort repository;
    private final UserProfileApplicationMapper mapper;
    
    private static final String ERR_BAD_REQUEST = "USR_400";
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "firstName", "lastName", "phone");

    public PageResponse<UserProfileDTO> execute(UserProfileFilter filter, PageQuery query) {
        if (query.size() > 50) {
            throw new BadRequestException(ERR_BAD_REQUEST, "Page size cannot exceed 50");
        }
        
        if (query.page() < 0) {
            throw new BadRequestException(ERR_BAD_REQUEST, "Page number cannot be negative");
        }

        if (!ALLOWED_SORT_FIELDS.contains(query.sortBy())) {
            throw new BadRequestException(ERR_BAD_REQUEST, "Sorting by '" + query.sortBy() + "' is not allowed");
        }

        PageResponse<UserProfile> domainResponse = repository.findAll(filter, query);

        List<UserProfileDTO> dtos = domainResponse.content().stream()
                .map(mapper::toDTO)
                .toList();

        return new PageResponse<>(dtos, domainResponse.totalElements(), domainResponse.totalPages());
    }
}
