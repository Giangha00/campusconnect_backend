package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.LoginRequest;
import com.example.campusconnet_backend.entity.Admin;
import com.example.campusconnet_backend.service.AdminService;
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

    @GetMapping
    public List<Admin> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Admin getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public Admin create(@RequestBody Admin admin) {
        return service.save(admin);
    }

    @PutMapping("/{id}")
    public Admin update(@PathVariable String id, @RequestBody Admin data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<Admin> login(@RequestBody LoginRequest request) {
        try {
            Admin admin = service.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
