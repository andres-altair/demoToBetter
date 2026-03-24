package com.andres.demotobetter.modules.security.infrastructure.security;

import com.andres.demotobetter.modules.security.domain.repository.UserSecurityRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Class that implements UserDetailsService.
 * 
 * @author andres
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserSecurityRepositoryPort userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Load failed: No user found with the email: {}", email);
                    return new UsernameNotFoundException("User not found");
                });

        return new UserDetailsImpl(user);
    }
}
