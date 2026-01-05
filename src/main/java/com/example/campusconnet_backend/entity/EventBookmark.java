package com.example.campusconnet_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(
        name = "event_bookmarks",
        uniqueConstraints = @UniqueConstraint(name="uk_bookmarks_user_event", columnNames = {"user_id","event_id"})
)
public class EventBookmark {

    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id", nullable = false)
    private Event event;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
