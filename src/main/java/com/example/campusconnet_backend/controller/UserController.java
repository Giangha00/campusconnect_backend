package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<User> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @PostMapping
    public User create(@RequestBody User u) {
        if (u.getId() == null || u.getId().isBlank()) {
            u.setId(UUID.randomUUID().toString());
        }
        return repo.save(u);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User data) {
        User u = getById(id);

        u.setUsername(data.getUsername());
        u.setPassword(data.getPassword());
        u.setName(data.getName());
        u.setEmail(data.getEmail());
        u.setRole(data.getRole());
        u.setDepartment(data.getDepartment());
        u.setYear(data.getYear());

        return repo.save(u);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}
