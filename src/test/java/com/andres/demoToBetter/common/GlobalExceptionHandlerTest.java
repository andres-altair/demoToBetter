package com.andres.demotobetter.common;
/**package com.andres.demoToBetter.common;

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

import com.andres.demotobetter.common.exception.handler.GlobalExceptionHandler;
@WebMvcTest(controllers = FakeErrorController.class)
class GlobalExceptionHandlerTest {
@Autowired
    private MockMvc mockMvc;
    @Import(GlobalExceptionHandler.class)
    static class Config {}

    @SuppressWarnings("null")
    @Test
    void whenValidationFails_Returns400WithErrorDTO() throws Exception {
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
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid application"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.path").value("/test/errors/validation"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.traceId").exists());
    }

    @Test
    void whenRuntimeExceptionThrown_returns500() throws Exception{
        mockMvc.perform(get("/test/errors/runtime"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.code").value("SYS_500"))
        .andExpect(jsonPath("$.message").value("Internal error"))
        .andExpect(jsonPath("$.detail").value("Runtime boom"))
        .andExpect(jsonPath("$.path").value("/test/errors/runtime"))
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.traceId").exists());
    }

    @Test
    void whenUnexpectedExceptionThrown_Returns500() throws Exception {
        mockMvc.perform(get("/test/errors/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.code").value("SYS_500"))
                .andExpect(jsonPath("$.message").value("Internal error"))
                .andExpect(jsonPath("$.detail").value("Unexpected boom"))
                .andExpect(jsonPath("$.path").value("/test/errors/unexpected"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.traceId").exists());
    }

    @Test
    void whenNotFoundExceptionThrown_Returns404() throws Exception {
        mockMvc.perform(get("/test/errors/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Resource not found"))
                .andExpect(jsonPath("$.code").value("USR_404"))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.path").value("/test/errors/notfound"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.traceId").exists());
    }

    @Test
    void whenBadRequestExceptionThrown_Returns400() throws Exception {
        mockMvc.perform(get("/test/errors/badrequest"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid application"))
                .andExpect(jsonPath("$.code").value("USR_400"))
                .andExpect(jsonPath("$.message").value("Invalid input"))
                .andExpect(jsonPath("$.path").value("/test/errors/badrequest"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.traceId").exists());
    }

    @Test
    void whenConflictExceptionThrown_Returns409() throws Exception {
        mockMvc.perform(get("/test/errors/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value("Conflict in the operation"))
                .andExpect(jsonPath("$.code").value("USR_409"))
                .andExpect(jsonPath("$.message").value("Conflict occurred"))
                .andExpect(jsonPath("$.path").value("/test/errors/conflict"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.traceId").exists());
    }
}*/
