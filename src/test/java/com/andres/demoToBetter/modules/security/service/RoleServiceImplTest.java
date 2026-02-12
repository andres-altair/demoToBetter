package com.andres.demotobetter.modules.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.entity.Role;
import com.andres.demotobetter.modules.security.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    private Role userRole = new Role(1L, "USER", null);
    private Role adminRole = new Role(2L, "ADMIN", null);

    @Test
    void resolveRoles_WhenRoleNameIsNull_ReturnsDefaultUser() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        Set<Role> result = roleService.resolveRoles(null);

        assertEquals(1, result.size());
        assertTrue(result.contains(userRole));
        verify(roleRepository).findByName("USER");
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void resolveRoles_WhenRoleNameIsEmpty_ReturnsRoles() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        Set<Role> result = roleService.resolveRoles(Set.of());

        assertEquals(1, result.size());
        assertTrue(result.contains(userRole));
        verify(roleRepository).findByName("USER");
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void resolveRoles_WhenValidRoleNames_ReturnsRoles() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));

        Set<Role> result = roleService.resolveRoles(Set.of("USER", "ADMIN"));

        assertEquals(2, result.size());
        assertTrue(result.contains(userRole));
        assertTrue(result.contains(adminRole));
        verify(roleRepository).findByName("USER");
        verify(roleRepository).findByName("ADMIN");
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void resolveRoles_WhenDefaultUserRoleNotFound_ThrowsException() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> roleService.resolveRoles(null));

        verify(roleRepository).findByName("USER");
    }

    @Test
    void resolveRoles_WhenRoleNotFound_ThrowsException() {
        when(roleRepository.findByName("ADMINN")).thenReturn(Optional.empty());

        Set<String> roles = Set.of("ADMINN");
        assertThrows(BadRequestException.class, () -> roleService.resolveRoles(roles));

        verify(roleRepository).findByName("ADMINN");
    }
}