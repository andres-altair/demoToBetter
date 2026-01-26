package com.andres.demoToBetter.modules.users.service;

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
import com.andres.demotobetter.common.exception.custom.ConflictException;
import com.andres.demotobetter.common.exception.custom.NotFoundException;
import com.andres.demotobetter.modules.users.dto.UserFilterDTO;
import com.andres.demotobetter.modules.users.model.User;
import com.andres.demotobetter.modules.users.repository.UserRepository;
import com.andres.demotobetter.modules.users.service.UserService;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service; 


    @Test
    void findAll_WhenPageSizeTooLarge_ThrowException(){
        Pageable pageable = PageRequest.of(0, 100);
        UserFilterDTO userFilterDTO = new UserFilterDTO();

        assertThrows(BadRequestException.class,
            () -> service.findAll(userFilterDTO,pageable));
    }

    @Test
    void findAll_WhenPageNumberNegative_ThrowException() {
        Pageable pageable = mock(Pageable.class);

        when(pageable.getPageSize()).thenReturn(10);
        when(pageable.getPageNumber()).thenReturn(-1);

        UserFilterDTO filter = new UserFilterDTO();

        assertThrows(BadRequestException.class,
            () -> service.findAll(filter, pageable));
    }

    @Test
    void findAll_WhenSortingFieldNotAllowed_ThrowException() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("invalidField"));
        UserFilterDTO userFilterDTO = new UserFilterDTO();

        assertThrows(BadRequestException.class,
                () -> service.findAll(userFilterDTO, pageable));
    }

    @SuppressWarnings({"unchecked","null"})
    @Test
    void findAll_WithFilters_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        UserFilterDTO filter = new UserFilterDTO();
        filter.setUsername("and");
        filter.setEmail("mail");

        when(repository.findAll(any(Specification.class), eq(pageable)))
            .thenReturn(Page.empty());

        Page<User> result = service.findAll(filter, pageable);

        assertNotNull(result);
        verify(repository).findAll(any(Specification.class), eq(pageable));
        verifyNoMoreInteractions(repository);
    }


    @Test
    void findById_UserExists_ReturnUser(){
        User user = new User(1L, "andres", "andres@mail.com");

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        User result = service.findById(1L);

        assertNotNull(result,"Debe existir");
        assertEquals("andres",result.getUsername(),"Debe coincidir");
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_WhenUserNotExist_ThrowException(){
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(99L));
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_WhenIdNull_ThrowException(){
        assertThrows(BadRequestException.class, 
            () -> service.findById(null));
    }

    @Test
    void save_WhenEmailNotExist_SaveUser(){
        User newUser = new User(null, "andres", "andres@mail.com");
        when(repository.existsByEmail("andres@mail.com")).thenReturn(false);
        User savedUser = new User(1L, "andres", "andres@mail.com");
        when(repository.save(newUser)).thenReturn(savedUser);

        User result = service.save(newUser);

        assertNotNull(result.getId(), "El usuario guardado debe tener ID");
        assertEquals("andres", result.getUsername());
        assertEquals("andres@mail.com", result.getEmail());
        verify(repository).existsByEmail("andres@mail.com");
        verify(repository).save(newUser);
        verifyNoMoreInteractions(repository);
    }

    @SuppressWarnings("null")
    @Test
    void save_WhenEmailExists_ThrowException() {
        User newUser = new User(null, "andres", "andres@mail.com");
        when(repository.existsByEmail("andres@mail.com")).thenReturn(true);

        RuntimeException ex = assertThrows(
            ConflictException.class,
            ()->service.save(newUser),"Debe lanzar excepcion si el email ya existe"
        );
        assertEquals("Email already exists", ex.getMessage());
        verify(repository).existsByEmail("andres@mail.com");
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void delete_WhenIdNull_ThrowException(){
        assertThrows(BadRequestException.class, 
            ()-> service.delete(null));
    }

    @Test
    void delete_WhenUserNotExist_ThrowException(){
        when(repository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(NotFoundException.class,
            ()-> service.delete(1L),"Debe lanzar excepción si el usuario no existe"
        );
        assertEquals("User with ID 1 does not exist", ex.getMessage());
        verify(repository).existsById(anyLong());
        verify(repository,never()).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void delete_WhenUserExist_DeleteSuccessfully(){
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findByUsername_WhenNull_ThrowException(){
        assertThrows(BadRequestException.class, 
            () -> service.findByUsername(null));
    }

    @Test
    void findByUsername_WhenUserNotExist_ReturnEmpty(){
        when(repository.findByUsername("aa")).thenReturn(Optional.empty());

        Optional<User> result = service.findByUsername("aa");

        assertTrue(result.isEmpty(),"No deberia encontrsr ningun usuario");
        verify(repository).findByUsername("aa");
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findByUsername_WhenUserExists_ReturnUser(){
        User user = new User(1L, "andres", "andres@mail.com");

        when(repository.findByUsername("andres")).thenReturn(Optional.of(user));

        Optional<User> result = service.findByUsername("andres");

        assertTrue(result.isPresent(),"El usuario deberia existir");
        assertEquals("andres", result.get().getUsername());
        assertEquals("andres@mail.com", result.get().getEmail());
        verify(repository).findByUsername("andres");
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update_WhenIdNull_ThrowException(){
        User updated = new User(null, "new", "new@mail.com");

        assertThrows(BadRequestException.class, 
            ()->service.update(null,updated));
    }

    @SuppressWarnings("null")
    @Test
    void update_WhenUserNotExist_ThrowException(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        User updated = new User(null, "new", "new@mail.com");

        RuntimeException ex = assertThrows(NotFoundException.class,
            ()->service.update(1L, updated),
            "Debe lanzar excepción si el usuario no existe");
        
        assertEquals("User with ID 1 does not exist", ex.getMessage());
        verify(repository).findById(1L);
        verify(repository,never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @SuppressWarnings("null")
    @Test
    void update_WhenEmailAlreadyExists_ThrowException() {
        User existing = new User(1L, "andres", "andres@mail.com");
        User updated = new User(null, "andresNuevo", "otro@mail.com");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail("otro@mail.com")).thenReturn(true);

        RuntimeException ex = assertThrows(ConflictException.class,
            ()-> service.update(1L, updated) , "Debe lanzar excepción si el email ya existe"
        );
        assertEquals("Email already exists", ex.getMessage());
        verify(repository).findById(1L);
        verify(repository).existsByEmail("otro@mail.com");
        verify(repository,never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update_WhenValidData_UpdateSuccessfully(){
        User existing = new User(1L, "andres", "andres@mail.com");
        User updated = new User(null, "nuevoNombre", "nuevo@mail.com");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail("nuevo@mail.com")).thenReturn(false);
        User saved = new User(1L, "nuevoNombre", "nuevo@mail.com");
        when(repository.save(existing)).thenReturn(saved);
        User result = service.update(1L, updated);
        
        assertEquals("nuevoNombre", result.getUsername());
        assertEquals("nuevo@mail.com", result.getEmail());
        verify(repository).findById(1L);
        verify(repository).existsByEmail("nuevo@mail.com");
        verify(repository).save(existing);
        verifyNoMoreInteractions(repository);
    }
}