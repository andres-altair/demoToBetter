package com.andres.demotobetter.modules.users.domain.model;

public record PageQuery(int page, int size, String sortBy, String direction) {}
