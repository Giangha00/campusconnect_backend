package com.example.campusconnet_backend.dto;

import com.example.campusconnet_backend.model.EventCategory;
import com.example.campusconnet_backend.model.EventStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateRequest {
    
    @Size(max = 36, message = "Organizer ID must not exceed 36 characters")
    private String organizerId;
    
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    private String description;
    
    private Instant startDate;
    
    private Instant endDate;
    
    @Size(max = 200, message = "Venue must not exceed 200 characters")
    private String venue;
    
    private EventCategory category;
    
    private EventStatus status;
    
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
    
    private Boolean registrationRequired;
    
    private Integer capacity;
    
    private Instant registrationStart;
    
    private Instant registrationEnd;
}
