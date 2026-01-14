package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Admin;
import com.example.campusconnet_backend.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    private final AdminRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
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
        
        // Hash password trước khi lưu
        if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
            // Chỉ hash nếu password chưa được hash (không bắt đầu bằng $2a$ hoặc $2b$)
            if (!admin.getPassword().startsWith("$2a$") && !admin.getPassword().startsWith("$2b$")) {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
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
        
        // Hash password nếu có thay đổi và chưa được hash
        if (data.getPassword() != null && !data.getPassword().isBlank()) {
            // Chỉ hash nếu password chưa được hash (không bắt đầu bằng $2a$ hoặc $2b$)
            if (!data.getPassword().startsWith("$2a$") && !data.getPassword().startsWith("$2b$")) {
                admin.setPassword(passwordEncoder.encode(data.getPassword()));
            } else {
                // Nếu đã được hash rồi thì giữ nguyên
                admin.setPassword(data.getPassword());
            }
        }
        
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
