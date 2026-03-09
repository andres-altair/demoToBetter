package com.andres.demotobetter.modules.users.domain.model;

import java.util.List;

public record PageResponse<T>(List<T> content, long totalElements, int totalPages) {}
