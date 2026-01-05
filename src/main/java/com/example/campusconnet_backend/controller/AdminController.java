package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.Admin;
import com.example.campusconnet_backend.repository.AdminRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminRepository repo;

    public AdminController(AdminRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Admin> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Admin getById(@PathVariable String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + id));
    }

    @PostMapping
    public Admin create(@RequestBody Admin admin) {
        if (admin.getId() == null) {
            admin.setId(UUID.randomUUID().toString());
        }
        return repo.save(admin);
    }

    @PutMapping("/{id}")
    public Admin update(@PathVariable String id, @RequestBody Admin data) {
        Admin admin = getById(id);

        admin.setUsername(data.getUsername());
        admin.setPassword(data.getPassword());
        admin.setName(data.getName());
        admin.setEmail(data.getEmail());
        admin.setRole(data.getRole());

        return repo.save(admin);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}
