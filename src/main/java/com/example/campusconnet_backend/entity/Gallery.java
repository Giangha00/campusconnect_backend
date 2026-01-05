package com.example.campusconnet_backend.entity;

import com.example.campusconnet_backend.model.EventCategory;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name="gallery")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event; // nullable

    @Column(name="image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name="alt_text", length = 200)
    private String altText;

    @Column(nullable = false, length = 10)
    private String year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EventCategory category;

    @Column(name="event_name", length = 200)
    private String eventName;

    private LocalDate date;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;
}
