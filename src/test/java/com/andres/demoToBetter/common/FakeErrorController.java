/**package com.andres.demoToBetter.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.common.exception.custom.ConflictException;
import com.andres.demotobetter.common.exception.custom.NotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/test/errors")
class FakeErrorController {

    @PostMapping("/validation")
    public void triggerValidation(@Valid @RequestBody FakeDTO dto) {
        //Empty method is only used for teting validations
    }

    @GetMapping("/runtime")
    public void triggerRuntime() {
        throw new RuntimeException("Runtime boom");
    }

    @GetMapping("/unexpected")
    public void triggerUnexpected() throws Exception {
        throw new Exception("Unexpected boom");
    }

    @GetMapping("/notfound")
    public void triggerNotFound() {
        throw new NotFoundException("USR_404", "User not found");
    }

    @GetMapping("/badrequest")
    public void triggerBadRequest() {
        throw new BadRequestException("USR_400", "Invalid input");
    }

    @GetMapping("/conflict")
    public void triggerConflict() {
        throw new ConflictException("USR_409", "Conflict occurred");
    }
}
record FakeDTO(
        @NotBlank(message = "username required") String username,
        @Email(message = "email invalid") String email
) {}*/