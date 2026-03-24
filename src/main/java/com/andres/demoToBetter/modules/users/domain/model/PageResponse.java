package com.andres.demotobetter.modules.users.domain.model;

import java.util.List;
/**
 * Class representing a page response.
 * 
 * @author andres
 */
public record PageResponse<T>(List<T> content, long totalElements, int totalPages) {}
