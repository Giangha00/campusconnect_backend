package com.example.campusconnet_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatusUpdateRequest {
    
    @NotNull(message = "Active status is required")
    private Boolean active;
}
