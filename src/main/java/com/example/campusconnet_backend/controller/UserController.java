package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.LoginRequest;
import com.example.campusconnet_backend.dto.UserCreateRequest;
import com.example.campusconnet_backend.dto.UserResponse;
import com.example.campusconnet_backend.dto.UserUpdateRequest;
import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Transactional
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // GET endpoints - return Response DTOs (without password)
    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAllResponses();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable String id) {
        return service.getResponseById(id);
    }

    // POST/PUT endpoints - return Response DTOs (without password)
    @PostMapping
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        User user = service.create(request);
        return service.getResponseById(user.getId());
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable String id, @Valid @RequestBody UserUpdateRequest request) {
        User user = service.update(id, request);
        return service.getResponseById(user.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        try {
            UserResponse user = service.loginResponse(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
