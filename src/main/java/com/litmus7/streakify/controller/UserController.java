package com.litmus7.streakify.controller;

import com.litmus7.streakify.dto.UserRequestDTO;
import com.litmus7.streakify.dto.UserResponseDTO;
import com.litmus7.streakify.entity.User;
import com.litmus7.streakify.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequest) {
        User userEntity = new User();
        userEntity.setName(userRequest.getName());
        userEntity.setEmail(userRequest.getEmail());

        User savedUser = userService.registerUser(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedUser));
    }
    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable Long id){
        User user=userService.getUserById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully with id: "+ id);
    }

}
