package com.andres.demoToBetter.modules.users.integartion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.andres.demotobetter.modules.users.model.User;
import com.andres.demotobetter.modules.users.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository repository;
    @BeforeEach
    void setup(){
        repository.deleteAll();
    }

    @Test
    void createUser_FullFlow_WritesToDatabaseAndReturns201() throws Exception {
        String json = """
            {
                "username": "andres",
                "email": "andres@mail.com"
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("andres"))
                .andExpect(jsonPath("$.email").value("andres@mail.com"));
        
        User saved = repository.findByEmail("andres@mail.com").orElse(null);

        assertThat(saved).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("andres");
    }

    @Test
    void updateUser_FullFlow_UpdatesDatabaseAndReturns200() throws Exception {
    User existing = new User(null, "andres", "andres@mail.com");
    User saved = repository.save(existing);

    String json = """
        {
            "username": "nuevoNombre",
            "email": "nuevo@mail.com"
        }
        """;
        mockMvc.perform(put("/api/users/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(saved.getId()))
            .andExpect(jsonPath("$.username").value("nuevoNombre"))
            .andExpect(jsonPath("$.email").value("nuevo@mail.com"));
        
        User updated = repository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getUsername()).isEqualTo("nuevoNombre");
        assertThat(updated.getEmail()).isEqualTo("nuevo@mail.com");
    }
}
