package com.andres.demotobetter.modules.users.domain.model;
/**
 * Class representing a user profile filter.
 * 
 * @author andres
 */
public record UserProfileFilter(
    String firstName,
    String lastName,
    String phone
) {}
