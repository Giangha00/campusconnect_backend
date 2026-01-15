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
                user.getActive(),
                user.getDepartment(),
                user.getYear(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public User create(UserCreateRequest request) {
        // Check if username already exists
        if (repo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại. Vui lòng chọn một username mới.");
        }
        
        // Check if email already exists
        if (repo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email này đã đăng ký tài khoản. Vui lòng kiểm tra lại.");
        }
        
        Instant now = Instant.now();
        
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setActive(true); // Mặc định active khi tạo mới
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
        
        // Check username uniqueness if it's being changed
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (repo.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username đã tồn tại. Vui lòng chọn một username mới.");
            }
            user.setUsername(request.getUsername());
        }
        
        // Hash password nếu có thay đổi
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        
        // Check email uniqueness if it's being changed
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (repo.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email này đã đăng ký tài khoản. Vui lòng kiểm tra lại.");
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        
        // Cập nhật trạng thái active/inactive
        // Hỗ trợ cả Boolean active và String status ("Active"/"Inactive")
        Boolean newActiveStatus = null;
        
        if (request.getActive() != null) {
            // Nếu có trường active (boolean)
            newActiveStatus = request.getActive();
            System.out.println("✅ Received 'active' field (boolean): " + newActiveStatus);
        } else if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            // Nếu có trường status (string) - chuyển đổi từ "Active"/"Inactive" sang boolean
            String statusStr = request.getStatus().trim();
            if ("Active".equalsIgnoreCase(statusStr)) {
                newActiveStatus = true;
            } else if ("Inactive".equalsIgnoreCase(statusStr)) {
                newActiveStatus = false;
            } else {
                System.out.println("⚠️ WARNING: Invalid status value: '" + statusStr + "'. Expected 'Active' or 'Inactive'");
            }
            System.out.println("✅ Received 'status' field (string): '" + statusStr + "' -> converted to active: " + newActiveStatus);
        }
        
        if (newActiveStatus != null) {
            Boolean oldActiveStatus = user.getActive();
            user.setActive(newActiveStatus);
            System.out.println("✅ Updating user active status from " + oldActiveStatus + " to " + newActiveStatus + " for user ID: " + user.getId());
        } else {
            System.out.println("⚠️ WARNING: Neither 'active' nor 'status' field provided in update request for user ID: " + user.getId());
            System.out.println("⚠️ Keeping current active status: " + user.getActive());
            System.out.println("⚠️ To update active status, include either:");
            System.out.println("   - 'active' field (boolean: true/false), OR");
            System.out.println("   - 'status' field (string: 'Active'/'Inactive'), OR");
            System.out.println("   - Use PATCH /api/users/{id}/status endpoint");
        }
        
        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }
        if (request.getYear() != null) {
            user.setYear(request.getYear());
        }
        
        user.setUpdatedAt(Instant.now());
        
        // Save và flush để đảm bảo thay đổi được lưu vào database ngay lập tức
        User savedUser = repo.save(user);
        repo.flush(); // Force flush to database
        
        System.out.println("DEBUG: User saved successfully. Active status in DB: " + savedUser.getActive() + " for user ID: " + savedUser.getId());
        
        // Verify sau khi save
        User verifiedUser = repo.findById(savedUser.getId()).orElse(null);
        if (verifiedUser != null) {
            System.out.println("DEBUG: Verified active status after save: " + verifiedUser.getActive() + " for user ID: " + verifiedUser.getId());
        }
        
        return savedUser;
    }

    @Transactional
    public User updateStatus(String id, Boolean active) {
        User user = getById(id);
        Boolean oldActiveStatus = user.getActive();
        user.setActive(active);
        user.setUpdatedAt(Instant.now());
        
        System.out.println("DEBUG: Updating user status from " + oldActiveStatus + " to " + active + " for user ID: " + user.getId());
        
        User savedUser = repo.save(user);
        repo.flush();
        
        System.out.println("DEBUG: User status updated successfully. Active status: " + savedUser.getActive() + " for user ID: " + savedUser.getId());
        
        return savedUser;
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
