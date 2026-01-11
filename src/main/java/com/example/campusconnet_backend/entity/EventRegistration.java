package com.example.campusconnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(
        name = "event_registrations",
        uniqueConstraints = @UniqueConstraint(name="uk_registrations_user_event", columnNames = {"user_id","event_id"})
)
public class EventRegistration {

    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id", nullable = false)
    @JsonIgnore
    private Event event;

    @Column(name="ticket_number", nullable = false, unique = true, length = 50)
    private String ticketNumber;

    @Column(name="registration_date", nullable = false)
    private Instant registrationDate;

    @Column(name="checked_in", nullable = false)
    private Boolean checkedIn;

    @Column(name="checked_in_at")
    private Instant checkedInAt;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;
}
