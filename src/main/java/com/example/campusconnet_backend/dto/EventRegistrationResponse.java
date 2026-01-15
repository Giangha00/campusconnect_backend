package com.example.campusconnet_backend.dto;

import java.time.Instant;

public class EventRegistrationResponse {
    private String id;
    private String userId;
    private Long eventId;
    private String ticketNumber;
    private Instant registrationDate;
    private Boolean checkedIn;
    private Instant checkedInAt;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructors
    public EventRegistrationResponse() {}

    public EventRegistrationResponse(String id, String userId, Long eventId, String ticketNumber,
                                     Instant registrationDate, Boolean checkedIn, Instant checkedInAt,
                                     Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.ticketNumber = ticketNumber;
        this.registrationDate = registrationDate;
        this.checkedIn = checkedIn;
        this.checkedInAt = checkedInAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public Instant getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(Instant checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
