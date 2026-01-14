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
public class AdminUpdateRequest {
    
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
}
