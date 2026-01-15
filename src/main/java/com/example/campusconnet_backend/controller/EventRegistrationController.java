package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.EventRegistrationResponse;
import com.example.campusconnet_backend.entity.EventRegistration;
import com.example.campusconnet_backend.service.EventRegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-registrations")
@CrossOrigin(origins = "*")
@org.springframework.transaction.annotation.Transactional
public class EventRegistrationController {

    private final EventRegistrationService service;

    public EventRegistrationController(EventRegistrationService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventRegistrationResponse> getAll(@RequestParam(required = false) String userId) {
        List<EventRegistration> registrations;
        if (userId != null && !userId.isBlank()) {
            registrations = service.getByUserId(userId);
        } else {
            registrations = service.getAll();
        }
        
        // Convert to DTO with userId and eventId
        return registrations.stream().map(reg -> {
            EventRegistrationResponse response = new EventRegistrationResponse();
            response.setId(reg.getId());
            response.setUserId(reg.getUser() != null ? reg.getUser().getId() : null);
            response.setEventId(reg.getEvent() != null ? reg.getEvent().getId() : null);
            response.setTicketNumber(reg.getTicketNumber());
            response.setRegistrationDate(reg.getRegistrationDate());
            response.setCheckedIn(reg.getCheckedIn());
            response.setCheckedInAt(reg.getCheckedInAt());
            response.setCreatedAt(reg.getCreatedAt());
            response.setUpdatedAt(reg.getUpdatedAt());
            return response;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public EventRegistrationResponse register(@RequestBody java.util.Map<String, Object> request) {
        try {
            String userId = (String) request.get("userId");
            Object eventIdObj = request.get("eventId");
            Long eventId;
            
            // Validate userId
            if (userId == null || userId.isBlank()) {
                throw new RuntimeException("userId is required");
            }
            
            // Validate and parse eventId
            if (eventIdObj == null) {
                throw new RuntimeException("eventId is required");
            }
            
            if (eventIdObj instanceof Integer) {
                eventId = ((Integer) eventIdObj).longValue();
            } else if (eventIdObj instanceof Long) {
                eventId = (Long) eventIdObj;
            } else if (eventIdObj instanceof String) {
                try {
                    eventId = Long.parseLong((String) eventIdObj);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid eventId format: " + eventIdObj);
                }
            } else {
                throw new RuntimeException("Invalid eventId format: expected Integer, Long, or String");
            }
            
            EventRegistration reg = service.save(userId, eventId);
            
            // Convert to DTO
            EventRegistrationResponse response = new EventRegistrationResponse();
            response.setId(reg.getId());
            response.setUserId(reg.getUser() != null ? reg.getUser().getId() : userId);
            response.setEventId(reg.getEvent() != null ? reg.getEvent().getId() : eventId);
            response.setTicketNumber(reg.getTicketNumber());
            response.setRegistrationDate(reg.getRegistrationDate());
            response.setCheckedIn(reg.getCheckedIn());
            response.setCheckedInAt(reg.getCheckedInAt());
            response.setCreatedAt(reg.getCreatedAt());
            response.setUpdatedAt(reg.getUpdatedAt());
            return response;
        } catch (RuntimeException e) {
            // Re-throw to be handled by GlobalExceptionHandler
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating registration: " + e.getMessage(), e);
        }
    }

    @PutMapping("/{id}/checkin")
    public EventRegistration checkIn(@PathVariable String id) {
        return service.checkIn(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping
    public void deleteByUserAndEvent(
            @RequestParam String userId,
            @RequestParam Long eventId) {
        service.deleteByUserAndEvent(userId, eventId);
    }
}
