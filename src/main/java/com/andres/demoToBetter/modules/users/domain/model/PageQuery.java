package com.andres.demotobetter.modules.users.domain.model;
/**
 * Class representing a page query.
 * 
 * @author andres
 */
public record PageQuery(int page, int size, String sortBy, String direction) {}
