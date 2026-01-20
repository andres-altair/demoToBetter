package com.andres.demoToBetter.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.andres.demoToBetter.common.exception.handler.GlobalExceptionHandler;
@WebMvcTest(controllers = FakeErrorController.class)
public class GlobalExceptionHandlerTest {
@Autowired
    private MockMvc mockMvc;

    @Import(GlobalExceptionHandler.class)
    static class Config {}

    @Test
    void whenValidationFails_Returns400WithErrors() throws Exception {
        String invalidJson = """
            {
                "username": "",
                "email": "not-an-email"
            }
            """;
        mockMvc.perform(post("/test/errors/validation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.username").value("username required"))
            .andExpect(jsonPath("$.email").value("email invalid"));
    }

    @Test
    void whenRuntimeExceptionThrown_Returns400() throws Exception {
        mockMvc.perform(get("/test/errors/runtime"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Runtime boom"));
    }

    @Test
    void whenUnexpectedExceptionThrown_Returns500() throws Exception {
        mockMvc.perform(get("/test/errors/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Unexpected error: Unexpected boom"));
    }
}
