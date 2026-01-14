package com.example.campusconnet_backend.dto;

import com.example.campusconnet_backend.model.EventCategory;
import com.example.campusconnet_backend.model.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequest {
    
    @Size(max = 36, message = "Organizer ID must not exceed 36 characters")
    private String organizerId;
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    private String description;
    
    @NotNull(message = "Start date is required")
    private Instant startDate;
    
    private Instant endDate;
    
    @Size(max = 200, message = "Venue must not exceed 200 characters")
    private String venue;
    
    @NotNull(message = "Category is required")
    private EventCategory category;
    
    @NotNull(message = "Status is required")
    private EventStatus status;
    
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
    
    @NotNull(message = "Registration required flag is required")
    private Boolean registrationRequired;
    
    private Integer capacity;
    
    private Instant registrationStart;
    
    private Instant registrationEnd;
}
