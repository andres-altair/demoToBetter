package com.andres.demoToBetter.modules.users.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.andres.demoToBetter.modules.users.dto.UserCreateDTO;
import com.andres.demoToBetter.modules.users.dto.UserDTO;
import com.andres.demoToBetter.modules.users.dto.UserUpdateDTO;
import com.andres.demoToBetter.modules.users.mapper.UserMapper;
import com.andres.demoToBetter.modules.users.model.User;
import com.andres.demoToBetter.modules.users.service.UserService;
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserMapper userMapper;
    
    @Test
    void getAll_WhenUsersExist_Returns200AndList() throws Exception{
        User user = new User(1L, "andres", "andres@mail.com");
        UserDTO dto = new UserDTO(1L, "andres", "andres@mail.com");

        when(userService.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].username").value("andres"))
        .andExpect(jsonPath("$[0].email").value("andres@mail.com"));
    }

    @Test
    void getAll_WhenNoUsers_Return204() throws Exception{
        when(userService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
        .andExpect(status().isNoContent());
    }

    @Test
    void getById_WhenUserExit_Returns200AndUser() throws Exception{
        User user = new User(1L, "andres", "andres@mail.com");
        UserDTO dto = new UserDTO(1L, "andres", "andres@mail.com");

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        mockMvc.perform(get("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("andres"))
        .andExpect(jsonPath("$.email").value("andres@mail.com"));
    }

    @Test
    void getById_WhenUserNotExist_Returns404() throws Exception{
        when(userService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
        .andExpect(status().isNotFound());
    }

    @Test
    void create_WhenValidData_Returns201AndUser() throws Exception{
        User userEntity = new User(null, "andres", "andres@mail.com");
        User savedEntity = new User(1L, "andres", "andres@mail.com");
        UserDTO responseDTO = new UserDTO(1L, "andres", "andres@mail.com");

        when(userMapper.toEntity(any(UserCreateDTO.class))).thenReturn(userEntity);
        when(userService.save(userEntity)).thenReturn(savedEntity);
        when(userMapper.toDTO(savedEntity)).thenReturn(responseDTO);
        String json = """
                {
                    "username": "ansdres" ,
                    "email": "andres@mail.com"
                }
                """;
        mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("andres"))
        .andExpect(jsonPath("$.email").value("andres@mail.com"));
    }

    @Test
    void create_WhenNoValidData_Returns400() throws Exception{
        String invalidJson = """
                {
                    "username": "" ,
                    "email": "inavalid-email"
                }
                """;
        mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.username").exists())
        .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void delete_WhenUserExist_Returns204() throws Exception{
        User user = new User(1L, "andres", "andres@mail.com");

        when(userService.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/1"))
        .andExpect(status().isNoContent());

        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    @Test
    void delete_WhenUserNotExist_Returns404() throws Exception{
        when(userService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNotFound());

        verify(userService).findById(1L);
        verify(userService, never()).delete(any());
    }

    @Test
    void update_WhenValidData_Returns200AndUpdatedUser() throws Exception{
        User updatedEntity = new User(1L, "nuevoNombre", "nuevo@mail.com");
        UserDTO responseDTO = new UserDTO(1L, "nuevoNombre", "nuevo@mail.com");

        when(userMapper.toEntity(any(UserUpdateDTO.class))).thenReturn(updatedEntity);
        when(userService.update(eq(1L), any(User.class))).thenReturn(updatedEntity);
        when(userMapper.toDTO(updatedEntity)).thenReturn(responseDTO);

        String json  ="""
                {
                    "username": "nuevoNombre",
                    "email": "nuevo@mail.com"
                }
                """;
        mockMvc.perform(put("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").value("nuevoNombre"))
            .andExpect(jsonPath("$.email").value("nuevo@mail.com"));
    }

    @Test
    void update_WhenInvalidData_Return400() throws Exception{
        String invalidJson = """
        {
            "username": "",
            "email": "invalid-email"
        }
        """;
        mockMvc.perform(put("/api/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.username").exists())
        .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void update_WhenUserNotExist_Return404() throws Exception{
        User userEntity = new User(null, "nuevo", "nuevo@mail.com");

        when(userMapper.toEntity(any(UserUpdateDTO.class))).thenReturn(userEntity);
        when(userService.update(1L, userEntity))
        .thenThrow(new RuntimeException("User with ID 1 does not exist"));

        String json = """
            {
                "username": "nuevo",
                "email": "nuevo@mail.com"
            }
            """;
        mockMvc.perform(put("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("User with ID 1 does not exist"));
    }
}
