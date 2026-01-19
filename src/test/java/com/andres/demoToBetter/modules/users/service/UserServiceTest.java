package com.andres.demoToBetter.modules.users.service;

import com.andres.demoToBetter.modules.users.model.User;
import com.andres.demoToBetter.modules.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service; 

    @Test
    void findAll_UsersExist_ReturnsUserList() {
        List<User> mockUsers = List.of(
            new User(1L, "andres", "andres@mail.com"),
            new User(2L, "maria", "maria@mail.com")
        );

        when(repository.findAll()).thenReturn(mockUsers);

        List<User> result = service.findAll();

        assertEquals(2, result.size(), "Debe devolver 2 usuarios");
        assertEquals("andres", result.get(0).getUsername());
        assertEquals("maria", result.get(1).getUsername());

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_UserExists_ReturnUser(){
        User user = new User(1L, "andres", "andres@mail.com");

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = service.findById(1L);

        assertTrue(result.isPresent(),"Debe existir");
        assertEquals("andres",result.get().getUsername(),"Debe coincidir");
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_UserNotExist_ReturnEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = service.findById(99L);

        assertTrue(result.isEmpty(),"Debe estar vacio");
        verify(repository).findById(99L);
        verifyNoMoreInteractions(repository);
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

    @Test
    void save_WhenEmailExists_ThrowException() {
        User newUser = new User(null, "andres", "andres@mail.com");
        when(repository.existsByEmail("andres@mail.com")).thenReturn(true);

        RuntimeException ex = assertThrows(
            RuntimeException.class,
            ()->service.save(newUser),"Debe lanzar excepcion si el email ya existe"
        );
        assertEquals("Email already exists: andres@mail.com", ex.getMessage());
        verify(repository).existsByEmail("andres@mail.com");
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void delete_WhenUserNotExist_ThrowException(){
        when(repository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            ()-> service.delete(1L),"Debe lanzar excepción si el usuario no existe"
        );
        assertEquals("User with ID 1 does not exist", ex.getMessage());
        verify(repository).existsById(anyLong());
        verify(repository,never()).deleteById(any());
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
    void update_WhenUserNotExist_ThrowException(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            ()->service.update(1L, new User(null, "new", "new@mail.com")),
            "Debe lanzar excepción si el usuario no existe");
        assertEquals("User with ID 1 does not exist", ex.getMessage());
        verify(repository).findById(1L);
        verify(repository,never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update_WhenEmailAlreadyExists_ThrowException() {
        User existing = new User(1L, "andres", "andres@mail.com");
        User updated = new User(null, "andresNuevo", "otro@mail.com");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail("otro@mail.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
            ()-> service.update(1L, updated) , "Debe lanzar excepción si el email ya existe"
        );
        assertEquals("Email already exists: otro@mail.com", ex.getMessage());
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