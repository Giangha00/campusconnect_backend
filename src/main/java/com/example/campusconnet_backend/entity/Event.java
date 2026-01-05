package com.example.campusconnet_backend.entity;

import com.example.campusconnet_backend.model.EventCategory;
import com.example.campusconnet_backend.model.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "organizer_id")
//    private Admin organizer; // nullable (ON DELETE SET NULL)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private Admin organizer;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String description;

    @Column(name="start_date", nullable = false)
    private Instant startDate;

    @Column(name="end_date")
    private Instant endDate;

    @Column(length = 200)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status;

    @Column(name="image_url", length = 500)
    private String imageUrl;

    @Column(name="registration_required", nullable = false)
    private Boolean registrationRequired;

    private Integer capacity;

    @Column(name="registration_start")
    private Instant registrationStart;

    @Column(name="registration_end")
    private Instant registrationEnd;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;
}
