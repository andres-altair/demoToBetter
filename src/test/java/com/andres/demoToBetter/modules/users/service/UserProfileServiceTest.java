// package com.andres.demotobetter.modules.users.service;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.jpa.domain.Specification;

// import com.andres.demotobetter.common.exception.custom.BadRequestException;
// import com.andres.demotobetter.common.exception.custom.NotFoundException;
// import com.andres.demotobetter.modules.security.entity.UserSecurity;
// import com.andres.demotobetter.modules.security.service.UserSecurityService;
// import com.andres.demotobetter.modules.users.application.dto.UserProfileCreateDTO;
// import com.andres.demotobetter.modules.users.application.dto.UserProfileDTO;
// import com.andres.demotobetter.modules.users.application.dto.UserProfileFilterDTO;
// import com.andres.demotobetter.modules.users.infrastructure.persistence.entity.UserProfileEntity;
// import com.andres.demotobetter.modules.users.infrastructure.persistence.repository.UserProfileJpaRepository;
// import com.andres.demotobetter.modules.users.mapper.UserProfileMapper;

// import java.util.Optional;
// import java.util.Set;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class UserProfileServiceTest {

//     @Mock
//     private UserProfileJpaRepository repository;
//     @Mock
//     private UserProfileMapper mapper;
//     @Mock
//     private UserSecurityService userSecurityService;
//     @InjectMocks
//     private UserProfileServiceImpl service;

//     @Test
//     void findAll_WhenPageSizeTooLarge_ThrowException() {
//         Pageable pageable = PageRequest.of(0, 100);
//         UserProfileFilterDTO userProfileFilterDTO = new UserProfileFilterDTO();

//         BadRequestException ex = assertThrows(BadRequestException.class,
//                 () -> service.findAll(userProfileFilterDTO, pageable));
//         assertTrue(ex.getMessage().contains("Page size cannot exceed"));
//     }

//     @Test
//     void findAll_WhenPageNumberNegative_ThrowException() {
//         Pageable pageable = mock(Pageable.class);

//         when(pageable.getPageSize()).thenReturn(10);
//         when(pageable.getPageNumber()).thenReturn(-1);

//         UserProfileFilterDTO userProfileFilterDTO = new UserProfileFilterDTO();

//         assertThrows(BadRequestException.class,
//                 () -> service.findAll(userProfileFilterDTO, pageable));
//     }

//     @Test
//     void findAll_WhenSortingFieldNotAllowed_ThrowException() {
//         Pageable pageable = PageRequest.of(0, 10, Sort.by("invalidField"));
//         UserProfileFilterDTO userProfileFilterDTO = new UserProfileFilterDTO();

//         assertThrows(BadRequestException.class, () -> service.findAll(userProfileFilterDTO, pageable));
//         verify(repository, never()).findAll();
//     }

//     @SuppressWarnings({ "unchecked", "null" })
//     @Test
//     void findAll_WithFilters_ReturnsPage() {
//         Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
//         UserProfileFilterDTO userProfileFilterDTO = new UserProfileFilterDTO();
//         userProfileFilterDTO.setFirstName("and");
//         userProfileFilterDTO.setLastName("mol");
//         userProfileFilterDTO.setPhone("123");

//         when(repository.findAll(any(Specification.class), eq(pageable)))
//                 .thenReturn(Page.empty());

//         Page<UserProfileEntity> result = service.findAll(userProfileFilterDTO, pageable);

//         assertNotNull(result);
//         verify(repository).findAll(any(Specification.class), eq(pageable));
//         verifyNoMoreInteractions(repository);
//     }

//     @Test
//     void findById_UserExists_ReturnUser() {
//         UserProfileEntity userProfile = new UserProfileEntity(1L, "andres", "molina", "123", "avatarUrl", null);

//         when(repository.findById(1L)).thenReturn(Optional.of(userProfile));

//         UserProfileEntity result = service.findById(1L);

//         assertNotNull(result, "Debe existir");
//         assertEquals("andres", result.getFirstName(), "Debe coincidir");
//         verify(repository).findById(1L);
//         verifyNoMoreInteractions(repository);
//     }

//     @Test
//     void findById_WhenUserNotExist_ThrowException() {
//         when(repository.findById(99L)).thenReturn(Optional.empty());

//         assertThrows(NotFoundException.class, () -> service.findById(99L));
//         verify(repository).findById(anyLong());
//         verifyNoMoreInteractions(repository);
//     }

//     @SuppressWarnings("null")
//     @Test
//     void findById_WhenIdNull_ThrowException() {
//         assertThrows(BadRequestException.class, () -> service.findById(null));
//         verify(repository, never()).findById(null);
//         verifyNoMoreInteractions(repository);
//     }

//     @Test
//     void delete_WhenIdNull_ThrowException() {
//         assertThrows(BadRequestException.class, () -> service.delete(null));
//         verifyNoInteractions(userSecurityService);
//     }

//     @Test
//     void delete_WhenUserExist_DisableSuccessfully() {
//         doNothing().when(userSecurityService).disableUser(1L);

//         service.delete(1L);

//         verify(userSecurityService).disableUser(1L);
//         verifyNoMoreInteractions(userSecurityService);
//     }

//     @SuppressWarnings("null")
//     @Test
//     void update_WhenIdNull_ThrowException() {
//         UserProfileEntity updated = new UserProfileEntity();

//         assertThrows(BadRequestException.class, () -> service.update(null, updated));
//         verify(repository, never()).save(any());
//         verifyNoMoreInteractions(repository);
//     }

//     @SuppressWarnings("null")
//     @Test
//     void update_WhenUserNotExist_ThrowException() {
//         when(repository.findById(1L)).thenReturn(Optional.empty());

//         UserProfileEntity updated = new UserProfileEntity();

//         RuntimeException ex = assertThrows(NotFoundException.class, () -> service.update(1L, updated),
//                 "Debe lanzar excepción si el usuario no existe");

//         assertEquals("User with ID 1 does not exist", ex.getMessage());
//         verify(repository).findById(1L);
//         verify(repository, never()).save(any());
//         verifyNoMoreInteractions(repository);
//     }

//     @Test
//     void update_WhenValidData_UpdateSuccessfully() {
//         UserProfileEntity existing = new UserProfileEntity(1L, "andres", "molina", "123", "avatarUrl", null);
//         UserProfileEntity updated = new UserProfileEntity(1L, "nuevoNombre", "nuevoApellido", "123", "avatarUrl", null);
//         UserProfileEntity saved = new UserProfileEntity(1L, "nuevoNombre", "nuevoApellido", "123", "avatarUrl", null);

//         when(repository.findById(1L)).thenReturn(Optional.of(existing));
//         when(repository.save(existing)).thenReturn(saved);
//         UserProfileEntity result = service.update(1L, updated);

//         assertEquals("nuevoNombre", result.getFirstName());
//         assertEquals("nuevoApellido", result.getLastName());
//         assertNotSame(updated, existing);
//         verify(repository).findById(1L);
//         verify(repository).save(existing);
//         verifyNoMoreInteractions(repository);
//     }

//     @Test
//     void save_WhenValidData_SaveSuccessfully() {
//         UserProfileCreateDTO dto = new UserProfileCreateDTO();
//         dto.setEmail("test@example.com");
//         dto.setPassword("123456");
//         dto.setRoles(Set.of("USER"));
//         UserSecurity security = new UserSecurity();
//         UserProfileEntity entity = new UserProfileEntity();
//         UserProfileDTO responseDTO = new UserProfileDTO();

//         when(userSecurityService.createSecurityUser(dto.getEmail(), dto.getPassword(), dto.getRoles()))
//                 .thenReturn(security);
//         when(mapper.toEntity(dto)).thenReturn(entity);
//         when(repository.save(entity)).thenReturn(entity);
//         when(mapper.toDTO(entity)).thenReturn(responseDTO);

//         UserProfileDTO result = service.save(dto);

//         assertEquals(responseDTO, result);
//         assertNotNull(result);
//         verify(userSecurityService).createSecurityUser(dto.getEmail(), dto.getPassword(), dto.getRoles());
//         verify(mapper).toEntity(dto);
//         verify(repository).save(entity);
//         verify(mapper).toDTO(entity);
//         verifyNoMoreInteractions(userSecurityService, mapper, repository);
//     }
// }