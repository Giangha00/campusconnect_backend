package com.example.campusconnet_backend.dto;

import com.example.campusconnet_backend.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Size(min = 6, max = 255, message = "Password must be at least 6 characters")
    private String password;
    
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    private Role role;
    
    private Boolean active;
    
    // Hỗ trợ cả string status ("Active"/"Inactive") từ FE
    private String status;
    
    @Size(max = 36, message = "Department ID must not exceed 36 characters")
    private String departmentId; // Preferred: use department_id
    
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department; // Deprecated: for backward compatibility, will try to find by name
    
    @Size(max = 10, message = "Year must not exceed 10 characters")
    private String year;
}
