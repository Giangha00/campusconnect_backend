package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Admin;
import com.example.campusconnet_backend.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    private final AdminRepository repo;

    public AdminService(AdminRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Admin> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Admin getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + id));
    }

    @Transactional
    public Admin save(Admin admin) {
        Instant now = Instant.now();
        
        if (admin.getId() == null || admin.getId().isBlank()) {
            admin.setId(UUID.randomUUID().toString());
        }
        
        if (admin.getCreatedAt() == null) {
            admin.setCreatedAt(now);
        }
        admin.setUpdatedAt(now);
        
        return repo.save(admin);
    }

    @Transactional
    public Admin update(String id, Admin data) {
        Admin admin = getById(id);
        
        admin.setUsername(data.getUsername());
        admin.setPassword(data.getPassword());
        admin.setName(data.getName());
        admin.setEmail(data.getEmail());
        admin.setRole(data.getRole());
        admin.setUpdatedAt(Instant.now());
        
        return repo.save(admin);
    }

    @Transactional
    public void delete(String id) {
        repo.deleteById(id);
    }
}
