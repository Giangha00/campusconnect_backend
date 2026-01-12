package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.service.UserService;
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

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return service.save(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
