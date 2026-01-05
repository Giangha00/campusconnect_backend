package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public User getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User save(User user) {
        return repo.save(user);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
