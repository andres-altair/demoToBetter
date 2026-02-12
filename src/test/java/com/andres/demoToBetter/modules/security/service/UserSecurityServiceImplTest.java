package com.andres.demotobetter.modules.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.entity.Role;
import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;

@ExtendWith(MockitoExtension.class)
class UserSecurityServiceImplTest {
    @Mock
    private UserSecurityRepository userSecurityRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserSecurityServiceImpl service;

    @Test
    void createSecurityUser_WhenEmailExists_ThrowsException() {
        when(userSecurityRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(new UserSecurity()));

        Set<String> roles = Set.of("USER");
        assertThrows(BadRequestException.class,
                () -> service.createSecurityUser("test@mail.com", "1234", roles));

        verify(userSecurityRepository).findByEmail("test@mail.com");
        verifyNoMoreInteractions(userSecurityRepository, roleService, passwordEncoder);
    }

    @SuppressWarnings("null")
    @Test
    void createSecurityUser_WhenNotEmailExists_CreateUser() {
        when(userSecurityRepository.findByEmail("andres@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encoded1234");
        Role userRole = new Role(1L, "USER", null);
        when(roleService.resolveRoles(Set.of("USER"))).thenReturn(Set.of(userRole));

        UserSecurity savedUser = new UserSecurity();
        savedUser.setId(1L);
        savedUser.setEmail("andres@mail.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRoles(Set.of(userRole));
        when(userSecurityRepository.save(any(UserSecurity.class))).thenReturn(savedUser);

        UserSecurity result = service.createSecurityUser("andres@mail.com", "1234", Set.of("USER"));

        assertNotNull(result);
        assertEquals("andres@mail.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getRoles().contains(userRole));
        verify(userSecurityRepository).findByEmail("andres@mail.com");
        verify(passwordEncoder).encode("1234");
        verify(roleService).resolveRoles(Set.of("USER"));
        verify(userSecurityRepository).save(any(UserSecurity.class));
        verifyNoMoreInteractions(userSecurityRepository, roleService, passwordEncoder);
    }
}