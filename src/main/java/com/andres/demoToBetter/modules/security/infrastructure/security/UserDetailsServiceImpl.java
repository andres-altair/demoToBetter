package com.andres.demotobetter.modules.security.infrastructure.security;

import com.andres.demotobetter.modules.security.domain.repository.UserSecurityRepositoryPort;
import com.andres.demotobetter.modules.security.infrastructure.persistence.mapper.UserSecurityPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyectamos el Puerto del Dominio, no el JpaRepository
    private final UserSecurityRepositoryPort userRepository;
    // Necesitamos el mapper para pasar de Domain Model a Entity (que usa tu UserDetailsImpl)
    private final UserSecurityPersistenceMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("Loading user security details: {}", email);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Load failed: No user found with the email: {}", email);
                    return new UsernameNotFoundException("User not found");
                });

        log.debug("User {} successfully loaded from the database", email);
        return new UserDetailsImpl(user);
    }
}
