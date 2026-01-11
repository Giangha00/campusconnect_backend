package com.example.campusconnet_backend.entity;

import com.example.campusconnet_backend.model.FeedbackStatus;
import com.example.campusconnet_backend.model.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name="feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user; // nullable

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    @JsonIgnore
    private Event event; // nullable

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="user_type", nullable = false, length = 20)
    private UserType userType;

    @Column(nullable = false)
    private Integer rating;

    @Lob
    @Column(nullable = false)
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeedbackStatus status;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;
}
