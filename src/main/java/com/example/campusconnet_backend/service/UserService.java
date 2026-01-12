package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User save(User user) {
        Instant now = Instant.now();
        
        // Set ID nếu chưa có
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        }
        
        // Set timestamps
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(now);
        }
        user.setUpdatedAt(now);
        
        return repo.save(user);
    }

    @Transactional
    public User update(String id, User data) {
        User user = getById(id);
        
        user.setUsername(data.getUsername());
        user.setPassword(data.getPassword());
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        user.setRole(data.getRole());
        user.setDepartment(data.getDepartment());
        user.setYear(data.getYear());
        user.setUpdatedAt(Instant.now());
        
        return repo.save(user);
    }

    @Transactional
    public void delete(String id) {
        repo.deleteById(id);
    }
}
