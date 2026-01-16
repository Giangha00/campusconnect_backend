package com.example.campusconnet_backend.dto;

import com.example.campusconnet_backend.model.EventCategory;
import com.example.campusconnet_backend.model.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String organizerId;
    private OrganizerInfo organizer;
    private String title;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private String venue;
    private EventCategory category;
    private EventStatus status;
    private String imageUrl;
    private Boolean registrationRequired;
    private Integer capacity;
    private Instant registrationStart;
    private Instant registrationEnd;
    private String department; // Department name from organizer's department
    private Instant createdAt;
    private Instant updatedAt;
    
    // Nested class for organizer information
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrganizerInfo {
        private String id;
        private String name;
        private String username;
        private String email;
        private String department; // Department name from organizer's department
    }
}
