package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.ErrorResponse;
import com.example.campusconnet_backend.dto.LoginRequest;
import com.example.campusconnet_backend.dto.UserCreateRequest;
import com.example.campusconnet_backend.dto.UserResponse;
import com.example.campusconnet_backend.dto.UserStatusUpdateRequest;
import com.example.campusconnet_backend.dto.UserUpdateRequest;
import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // GET endpoints - return Response DTOs (without password)
    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAllResponses();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable String id) {
        return service.getResponseById(id);
    }

    // POST/PUT endpoints - return Response DTOs (without password)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateRequest request) {
        try {
            User user = service.create(request);
            return ResponseEntity.ok(service.getResponseById(user.getId()));
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations (unique constraint on username/email)
            String errorMessage = e.getMessage();
            System.err.println("DataIntegrityViolationException: " + errorMessage);
            if (errorMessage != null) {
                String lowerMessage = errorMessage.toLowerCase();
                if (lowerMessage.contains("username") || lowerMessage.contains("uk_") || 
                    lowerMessage.contains("unique") && lowerMessage.contains("username")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("Username đã tồn tại. Vui lòng chọn một username mới."));
                } else if (lowerMessage.contains("email") || lowerMessage.contains("uk_") ||
                          lowerMessage.contains("unique") && lowerMessage.contains("email")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("Email này đã đăng ký tài khoản. Vui lòng kiểm tra lại."));
                }
            }
            // Generic duplicate error
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Thông tin đăng ký đã tồn tại trong hệ thống."));
        } catch (RuntimeException e) {
            // Check if it's a duplicate error from service layer
            System.err.println("RuntimeException: " + e.getMessage());
            System.err.println("Exception class: " + e.getClass().getName());
            if (e.getMessage() != null && 
                (e.getMessage().contains("Username đã tồn tại") || 
                 e.getMessage().contains("Email này đã đăng ký tài khoản"))) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
            }
            // Check if it's a wrapped DataIntegrityViolationException
            Throwable cause = e.getCause();
            if (cause instanceof DataIntegrityViolationException) {
                String errorMessage = cause.getMessage();
                if (errorMessage != null) {
                    String lowerMessage = errorMessage.toLowerCase();
                    if (lowerMessage.contains("username") || lowerMessage.contains("uk_")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ErrorResponse("Username đã tồn tại. Vui lòng chọn một username mới."));
                    } else if (lowerMessage.contains("email") || lowerMessage.contains("uk_")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ErrorResponse("Email này đã đăng ký tài khoản. Vui lòng kiểm tra lại."));
                    }
                }
            }
            // Other errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage() != null ? e.getMessage() : "Lỗi khi tạo tài khoản"));
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("Unexpected Exception: " + e.getClass().getName());
            System.err.println("Exception message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Lỗi server khi tạo tài khoản: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody UserUpdateRequest request) {
        try {
            System.out.println("=== USER UPDATE REQUEST ===");
            System.out.println("User ID: " + id);
            System.out.println("Request active field (boolean): " + request.getActive());
            System.out.println("Request status field (string): " + request.getStatus());
            System.out.println("Request body: " + request.toString());
            
            if (request.getActive() == null && (request.getStatus() == null || request.getStatus().trim().isEmpty())) {
                System.out.println("⚠️ WARNING: Neither 'active' (boolean) nor 'status' (string) field provided!");
                System.out.println("⚠️ Backend will keep the current active status from database.");
                System.out.println("⚠️ To update active status, include one of:");
                System.out.println("   1. 'active' field (boolean: true/false), OR");
                System.out.println("   2. 'status' field (string: 'Active'/'Inactive'), OR");
                System.out.println("   3. Use PATCH /api/users/" + id + "/status endpoint");
            }
            
            User user = service.update(id, request);
            UserResponse response = service.getResponseById(user.getId());
            
            System.out.println("Updated user active status: " + response.getActive());
            System.out.println("=== END USER UPDATE ===");
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations (unique constraint on username/email)
            String errorMessage = e.getMessage();
            if (errorMessage != null) {
                if (errorMessage.contains("username") || errorMessage.contains("UK_")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("Username đã tồn tại. Vui lòng chọn một username mới."));
                } else if (errorMessage.contains("email") || errorMessage.contains("UK_")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("Email này đã đăng ký tài khoản. Vui lòng kiểm tra lại."));
                }
            }
            // Generic duplicate error
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Thông tin cập nhật đã tồn tại trong hệ thống."));
        } catch (RuntimeException e) {
            // Check if it's a duplicate error from service layer
            if (e.getMessage() != null && 
                (e.getMessage().contains("Username đã tồn tại") || 
                 e.getMessage().contains("Email này đã đăng ký tài khoản"))) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
            }
            // Other errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage() != null ? e.getMessage() : "Lỗi khi cập nhật tài khoản"));
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Lỗi server khi cập nhật tài khoản"));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @Valid @RequestBody UserStatusUpdateRequest request) {
        try {
            System.out.println("Received status update request for user ID: " + id);
            System.out.println("Request active field: " + request.getActive());
            
            User user = service.updateStatus(id, request.getActive());
            UserResponse response = service.getResponseById(user.getId());
            
            System.out.println("Updated user active status: " + response.getActive());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage() != null ? e.getMessage() : "Lỗi khi cập nhật trạng thái tài khoản"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Lỗi server khi cập nhật trạng thái tài khoản"));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        try {
            UserResponse user = service.loginResponse(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
