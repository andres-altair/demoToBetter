package com.andres.demoToBetter.modules.users.controller;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.andres.demoToBetter.modules.users.dto.UserCreateDTO;
import com.andres.demoToBetter.modules.users.dto.UserDTO;
import com.andres.demoToBetter.modules.users.dto.UserUpdateDTO;
import com.andres.demoToBetter.modules.users.mapper.UserMapper;
import com.andres.demoToBetter.modules.users.model.User;
import com.andres.demoToBetter.modules.users.service.UserService;

import jakarta.validation.Valid;

import java.util.List;
/**
 * REST controller for managing User resources.
 * @author andres
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Retrieves all users from the system.
     *
     * @return ResponseEntity containing a list of UserDTO objects or an empty response
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.findAll()
            .stream()
            .map(userMapper::toDTO)
            .toList();
        if (users.isEmpty()) {
        return ResponseEntity.noContent().build(); // 204
    }
    return ResponseEntity.ok(users); // 200
    }
    
    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user
     * @return ResponseEntity containing the UserDTO or a 404 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
    return userService.findById(id)
        .map(userMapper::toDTO)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
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
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return userService.findById(id)
        .map(user -> {
            userService.delete(id);
            return ResponseEntity.noContent().build(); // 204
        })
        .orElse(ResponseEntity.notFound().build()); // 404
    }

/**
 * Updates an existing user by its ID.
 *
 * @param id the ID of the user to update
 * @param dto the updated user data
 * @return ResponseEntity containing the updated UserDTO or a 404 status
 */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,@Valid @RequestBody UserUpdateDTO dto) {
        User user = userMapper.toEntity(dto);
        User updated = userService.update(id, user);
        UserDTO response = userMapper.toDTO(updated);
        return ResponseEntity.ok(response);
    }
}