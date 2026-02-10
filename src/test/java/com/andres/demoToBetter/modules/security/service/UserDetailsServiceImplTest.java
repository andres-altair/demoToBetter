package com.andres.demotobetter.modules.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.model.UserDetailsImpl;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock 
    private UserSecurityRepository userSecurityRepository; 
    @InjectMocks 
    private UserDetailsServiceImpl userDetailsService;
    @Test
    void loadUserByUsername_WhenUserExist_ReturnsUserDetails() {
        UserSecurity user = new UserSecurity(); 
        user.setEmail("andres@mail.com"); 
        user.setPassword("1234");

        when(userSecurityRepository.findByEmail("andres@mail.com")).thenReturn(Optional.of(user));

        var result = userDetailsService.loadUserByUsername("andres@mail.com");

        assertNotNull(result); 
        assertTrue(result instanceof UserDetailsImpl); 
        assertEquals("andres@mail.com", 
        result.getUsername()); 
        verify(userSecurityRepository).findByEmail("andres@mail.com");
        verifyNoMoreInteractions(userSecurityRepository);
    }

    @Test
    void loadUserByUsername_WhenUserNotExist_ThrowsException() {       
        when(userSecurityRepository.findByEmail("andres@mail.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("andres@mail.com");
        });
        verify(userSecurityRepository).findByEmail("andres@mail.com");
        verifyNoMoreInteractions(userSecurityRepository);
    }
}