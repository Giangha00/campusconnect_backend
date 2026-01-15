package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.dto.AdminCreateRequest;
import com.example.campusconnet_backend.dto.AdminResponse;
import com.example.campusconnet_backend.dto.AdminUpdateRequest;
import com.example.campusconnet_backend.entity.Admin;
import com.example.campusconnet_backend.model.Role;
import com.example.campusconnet_backend.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
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
    public List<AdminResponse> getAllResponses() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Admin getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + id));
    }

    @Transactional(readOnly = true)
    public AdminResponse getResponseById(String id) {
        Admin admin = getById(id);
        return toResponse(admin);
    }

    private AdminResponse toResponse(Admin admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getUsername(),
                admin.getName(),
                admin.getEmail(),
                admin.getRole(),
                admin.getActive(),
                admin.getCreatedAt(),
                admin.getUpdatedAt()
        );
    }

    @Transactional
    public Admin create(AdminCreateRequest request) {
        // Check if username already exists
        if (repo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại. Vui lòng chọn một username mới.");
        }
        
        // Check if email already exists
        if (repo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email này đã đăng ký tài khoản. Vui lòng kiểm tra lại.");
        }
        
        Instant now = Instant.now();
        
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID().toString());
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setRole(request.getRole());
        admin.setActive(true); // Mặc định active khi tạo mới
        admin.setCreatedAt(now);
        admin.setUpdatedAt(now);
        
        return repo.save(admin);
    }

    // Keep old method for backward compatibility if needed
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
    public Admin update(String id, AdminUpdateRequest request) {
        Admin admin = getById(id);
        
        // Check username uniqueness if it's being changed
        if (request.getUsername() != null && !request.getUsername().equals(admin.getUsername())) {
            if (repo.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username đã tồn tại. Vui lòng chọn một username mới.");
            }
            admin.setUsername(request.getUsername());
        }
        
        // Hash password nếu có thay đổi
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getName() != null) {
            admin.setName(request.getName());
        }
        
        // Check email uniqueness if it's being changed
        if (request.getEmail() != null && !request.getEmail().equals(admin.getEmail())) {
            if (repo.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email này đã đăng ký tài khoản. Vui lòng kiểm tra lại.");
            }
            admin.setEmail(request.getEmail());
        }
        
        // Không cho phép thay đổi role cho cả admin và faculty
        // Chỉ cho phép thay đổi trạng thái active/inactive
        if (request.getRole() != null) {
            Role currentRole = admin.getRole();
            if (Role.admin.equals(currentRole) || Role.faculty.equals(currentRole)) {
                throw new RuntimeException("Không thể thay đổi role của tài khoản quản trị (admin/faculty). Chỉ có thể thay đổi trạng thái active/inactive.");
            }
            admin.setRole(request.getRole());
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
            Boolean oldActiveStatus = admin.getActive();
            admin.setActive(newActiveStatus);
            System.out.println("✅ Updating admin active status from " + oldActiveStatus + " to " + newActiveStatus + " for admin ID: " + admin.getId());
        } else {
            System.out.println("⚠️ WARNING: Neither 'active' nor 'status' field provided in update request for admin ID: " + admin.getId());
            System.out.println("⚠️ Keeping current active status: " + admin.getActive());
        }
        
        admin.setUpdatedAt(Instant.now());
        
        // Save và flush để đảm bảo thay đổi được lưu vào database ngay lập tức
        Admin savedAdmin = repo.save(admin);
        repo.flush(); // Force flush to database
        
        System.out.println("DEBUG: Admin saved successfully. Active status in DB: " + savedAdmin.getActive() + " for admin ID: " + savedAdmin.getId());
        
        return savedAdmin;
    }

    @Transactional
    public Admin updateStatus(String id, Boolean active) {
        Admin admin = getById(id);
        Boolean oldActiveStatus = admin.getActive();
        admin.setActive(active);
        admin.setUpdatedAt(Instant.now());
        
        System.out.println("DEBUG: Updating admin status from " + oldActiveStatus + " to " + active + " for admin ID: " + admin.getId());
        
        Admin savedAdmin = repo.save(admin);
        repo.flush();
        
        System.out.println("DEBUG: Admin status updated successfully. Active status: " + savedAdmin.getActive() + " for admin ID: " + savedAdmin.getId());
        
        return savedAdmin;
    }

    @Transactional
    public void delete(String id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Admin login(String username, String password) {
        Admin admin = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        // Verify password
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        return admin;
    }

    @Transactional(readOnly = true)
    public AdminResponse loginResponse(String username, String password) {
        Admin admin = login(username, password);
        return toResponse(admin);
    }
}
