package com.example.campusconnet_backend.dto;

import com.example.campusconnet_backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private String id;
    private String username;
    // Password is NOT included for security
    private String name;
    private String email;
    private Role role;
    private Boolean active;
    private String department; // Department name from JOIN
    private Instant createdAt;
    private Instant updatedAt;
}
