package com.example.campusconnet_backend.dto;

import com.example.campusconnet_backend.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackCreateRequest {
    private String userId;
    private Long eventId;
    private String name;
    private String email;
    private UserType userType;
    private Integer rating;
    private String feedback;
    private String status;
}
