package com.andres.demotobetter.modules.security.domain.service;

// modules/security/domain/service/PasswordHasherPort.java
public interface PasswordHasherPort {
    String encode(String rawPassword);
}
