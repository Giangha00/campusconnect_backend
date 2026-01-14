package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.dto.UserCreateRequest;
import com.example.campusconnet_backend.dto.UserResponse;
import com.example.campusconnet_backend.dto.UserUpdateRequest;
import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllResponses() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public User getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserResponse getResponseById(String id) {
        User user = getById(id);
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getDepartment(),
                user.getYear(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public User create(UserCreateRequest request) {
        Instant now = Instant.now();
        
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setDepartment(request.getDepartment());
        user.setYear(request.getYear());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        return repo.save(user);
    }

    // Keep old method for backward compatibility if needed
    @Transactional
    public User save(User user) {
        Instant now = Instant.now();
        
        // Set ID nếu chưa có
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        }
        
        // Hash password trước khi lưu
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            // Chỉ hash nếu password chưa được hash (không bắt đầu bằng $2a$ hoặc $2b$)
            if (!user.getPassword().startsWith("$2a$") && !user.getPassword().startsWith("$2b$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        
        // Set timestamps
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(now);
        }
        user.setUpdatedAt(now);
        
        return repo.save(user);
    }

    @Transactional
    public User update(String id, UserUpdateRequest request) {
        User user = getById(id);
        
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        
        // Hash password nếu có thay đổi
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }
        if (request.getYear() != null) {
            user.setYear(request.getYear());
        }
        
        user.setUpdatedAt(Instant.now());
        
        return repo.save(user);
    }

    @Transactional
    public void delete(String id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User login(String username, String password) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        return user;
    }

    @Transactional(readOnly = true)
    public UserResponse loginResponse(String username, String password) {
        User user = login(username, password);
        return toResponse(user);
    }
}
