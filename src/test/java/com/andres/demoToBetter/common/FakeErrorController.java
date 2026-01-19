package com.andres.demoToBetter.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/test/errors")
class FakeErrorController {

    @PostMapping("/validation")
    public void triggerValidation(@Valid @RequestBody FakeDTO dto) {}

    @GetMapping("/runtime")
    public void triggerRuntime() {
        throw new RuntimeException("Runtime boom");
    }

    @GetMapping("/unexpected")
    public void triggerUnexpected() throws Exception {
        throw new Exception("Unexpected boom");
    }
}

record FakeDTO(
        @NotBlank(message = "username required") String username,
        @Email(message = "email invalid") String email
) {}
