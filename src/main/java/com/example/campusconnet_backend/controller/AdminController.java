package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.AdminCreateRequest;
import com.example.campusconnet_backend.dto.AdminResponse;
import com.example.campusconnet_backend.dto.AdminUpdateRequest;
import com.example.campusconnet_backend.dto.LoginRequest;
import com.example.campusconnet_backend.entity.Admin;
import com.example.campusconnet_backend.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
@Transactional
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    // GET endpoints - return Response DTOs (without password)
    @GetMapping
    public List<AdminResponse> getAll() {
        return service.getAllResponses();
    }

    @GetMapping("/{id}")
    public AdminResponse getById(@PathVariable String id) {
        return service.getResponseById(id);
    }

    // POST/PUT endpoints - return Response DTOs (without password)
    @PostMapping
    public AdminResponse create(@Valid @RequestBody AdminCreateRequest request) {
        Admin admin = service.create(request);
        return service.getResponseById(admin.getId());
    }

    @PutMapping("/{id}")
    public AdminResponse update(@PathVariable String id, @Valid @RequestBody AdminUpdateRequest request) {
        Admin admin = service.update(id, request);
        return service.getResponseById(admin.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<AdminResponse> login(@RequestBody LoginRequest request) {
        try {
            AdminResponse admin = service.loginResponse(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
