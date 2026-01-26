package com.andres.demotobetter.modules.users.controller;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.andres.demotobetter.modules.users.dto.UserCreateDTO;
import com.andres.demotobetter.modules.users.dto.UserDTO;
import com.andres.demotobetter.modules.users.dto.UserFilterDTO;
import com.andres.demotobetter.modules.users.dto.UserUpdateDTO;
import com.andres.demotobetter.modules.users.mapper.UserMapper;
import com.andres.demotobetter.modules.users.model.User;
import com.andres.demotobetter.modules.users.service.UserService;

import jakarta.validation.Valid;
/**
 * REST controller for managing User resources.
 * 
 * @author andres
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Retrieves a paginated list of users with optional filtering.
     * 
     * @param username optional filter to search users whose username contains the given value
     * @param email optional filter to search users whose email contains the given value
     * @param pageable pagination and sorting information automatically resolved by Spring
     * @return a paginated list of UserDTO objects
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAll(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String email,
        Pageable pageable) {

        UserFilterDTO filter = new UserFilterDTO();
        filter.setUsername(username);
        filter.setEmail(email);

        Page<UserDTO> users = userService.findAll(filter, pageable)
            .map(userMapper::toDTO);

        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user
     * @return ResponseEntity containing the UserDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        UserDTO user = userMapper.toDTO(userService.findById(id));
        return ResponseEntity.ok(user);
    }

    /**
     * Creates a new user.
     *
     * @param dto the user data to create
     * @return ResponseEntity containing the created UserDTO
     */
    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO dto) {
        User user = userMapper.toEntity(dto);
        User saved = userService.save(user);
        UserDTO response = userMapper.toDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete
     * @return ResponseEntity with appropriate HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing user by its ID.
     *
     * @param id  the ID of the user to update
     * @param dto the updated user data
     * @return ResponseEntity containing the updated UserDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        User user = userMapper.toEntity(dto);
        User updated = userService.update(id, user);
        UserDTO response = userMapper.toDTO(updated);
        return ResponseEntity.ok(response);
    }
}