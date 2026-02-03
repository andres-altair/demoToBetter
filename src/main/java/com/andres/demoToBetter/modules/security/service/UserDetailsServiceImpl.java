package com.andres.demotobetter.modules.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andres.demotobetter.modules.security.model.UserDetailsImpl;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserSecurityRepository userSecurityRepository;
    @Override
    public UserDetails loadUserByUsername(String email)  {
        var user = userSecurityRepository.findByEmail(email) 
        .orElseThrow(() -> new UsernameNotFoundException("User not found")); 
        return new UserDetailsImpl(user);
    }
}