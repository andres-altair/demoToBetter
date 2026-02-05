package com.andres.demotobetter.modules.users.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.common.exception.custom.NotFoundException;
import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.service.UserSecurityService;
import com.andres.demotobetter.modules.users.dto.UserProfileCreateDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileDTO;
import com.andres.demotobetter.modules.users.dto.UserProfileFilterDTO;
import com.andres.demotobetter.modules.users.entity.UserProfile;
import com.andres.demotobetter.modules.users.mapper.UserProfileMapper;
import com.andres.demotobetter.modules.users.repository.UserProfileRepository;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository repository;
    @Mock
    private UserProfileMapper mapper;
    @Mock
    private UserSecurityService userSecurityService ;
    @InjectMocks
    private UserProfileServiceImpl service;

    @Test
    void findAll_WhenPageSizeTooLarge_ThrowException() {
        Pageable pageable = PageRequest.of(0, 100);
        UserProfileFilterDTO userProfileFilterDTO = new UserProfileFilterDTO();

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> service.findAll(userProfileFilterDTO, pageable));
        assertTrue(ex.getMessage().contains("Page size cannot exceed"));
    }

    @Test
    void findAll_WhenPageNumberNegative_ThrowException() {
        Pageable pageable = mock(Pageable.class);

        when(pageable.getPageSize()).thenReturn(10);
        when(pageable.getPageNumber()).thenReturn(-1);

        UserProfileFilterDTO userProfileFilterDTO = new UserProfileFilterDTO();

        assertThrows(BadRequestException.class,
                () -> service.findAll(userProfileFilterDTO, pageable));
    }

    @Test
    void findAll_WhenSortingFieldNotAllowed_ThrowException() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("invalidField"));
        UserProfileFilterDTO userProfileFilterDTO = new UserProfileFilterDTO();

        assertThrows(BadRequestException.class, () -> service.findAll(userProfileFilterDTO, pageable));
        verify(repository, never()).findAll();
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    void findAll_WithFilters_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        UserProfileFilterDTO userProfileFilterDTO = new UserProfileFilterDTO();
        userProfileFilterDTO.setFirstName("and");
        userProfileFilterDTO.setLastName("mol");
        userProfileFilterDTO.setPhone("123");

        when(repository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(Page.empty());

        Page<UserProfile> result = service.findAll(userProfileFilterDTO, pageable);

        assertNotNull(result);
        verify(repository).findAll(any(Specification.class), eq(pageable));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_UserExists_ReturnUser() {
        UserProfile userProfile = new UserProfile(1L, "andres", "molina", "123", "avatarUrl", null);

        when(repository.findById(1L)).thenReturn(Optional.of(userProfile));

        UserProfile result = service.findById(1L);

        assertNotNull(result, "Debe existir");
        assertEquals("andres", result.getFirstName(), "Debe coincidir");
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_WhenUserNotExist_ThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(99L));
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_WhenIdNull_ThrowException() {
        assertThrows(BadRequestException.class, () -> service.findById(null));
        verify(repository, never()).findById(null);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void delete_WhenIdNull_ThrowException() {
        assertThrows(BadRequestException.class, () -> service.delete(null));
        verify(repository, never()).deleteById(null);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void delete_WhenUserNotExist_ThrowException() {
        when(repository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(NotFoundException.class,
                () -> service.delete(1L), "Debe lanzar excepción si el usuario no existe");
        assertEquals("User with ID 1 does not exist", ex.getMessage());
        verify(repository).existsById(anyLong());
        verify(repository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void delete_WhenUserExist_DeleteSuccessfully() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update_WhenIdNull_ThrowException() {
        UserProfile updated = new UserProfile();

        assertThrows(BadRequestException.class, () -> service.update(null, updated));
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @SuppressWarnings("null")
    @Test
    void update_WhenUserNotExist_ThrowException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        UserProfile updated = new UserProfile();

        RuntimeException ex = assertThrows(NotFoundException.class, () -> service.update(1L, updated),
                "Debe lanzar excepción si el usuario no existe");

        assertEquals("User with ID 1 does not exist", ex.getMessage());
        verify(repository).findById(1L);
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update_WhenValidData_UpdateSuccessfully() {
        UserProfile existing = new UserProfile(1L, "andres", "molina", "123", "avatarUrl", null);
        UserProfile updated = new UserProfile(1L, "nuevoNombre", "nuevoApellido", "123", "avatarUrl", null);
        UserProfile saved = new UserProfile(1L, "nuevoNombre", "nuevoApellido", "123", "avatarUrl", null);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(saved);
        UserProfile result = service.update(1L, updated);

        assertEquals("nuevoNombre", result.getFirstName());
        assertEquals("nuevoApellido", result.getLastName());
        assertNotSame(updated, existing);
        verify(repository).findById(1L);
        verify(repository).save(existing);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void save_WhenValidData_SaveSuccessfully(){
        UserProfileCreateDTO dto = new UserProfileCreateDTO(); 
        dto.setEmail("test@example.com"); 
        dto.setPassword("123456"); 
        dto.setRoles(Set.of("USER"));
        UserSecurity security = new UserSecurity(); 
        UserProfile entity = new UserProfile(); 
        UserProfileDTO responseDTO = new UserProfileDTO();

        when(userSecurityService.createSecurityUser(dto.getEmail(), dto.getPassword(), dto.getRoles()))
        .thenReturn(security);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(responseDTO);

        UserProfileDTO result = service.save(dto);

        assertEquals(responseDTO, result);
        assertNotNull(result);
        verify(userSecurityService).createSecurityUser( dto.getEmail(), dto.getPassword(), dto.getRoles() ); 
        verify(mapper).toEntity(dto); 
        verify(repository).save(entity); 
        verify(mapper).toDTO(entity); 
        verifyNoMoreInteractions(userSecurityService, mapper, repository); 
    }
}