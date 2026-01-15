package com.example.campusconnet_backend.dto;

import java.time.Instant;

public class EventBookmarkResponse {
    private String id;
    private String userId;
    private Long eventId;
    private Instant createdAt;

    // Constructors
    public EventBookmarkResponse() {}

    public EventBookmarkResponse(String id, String userId, Long eventId, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
