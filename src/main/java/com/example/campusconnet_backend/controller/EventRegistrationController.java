package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.EventRegistration;
import com.example.campusconnet_backend.service.EventRegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<EventRegistration> getAll() {
        return service.getAll();
    }

    @PostMapping
    public EventRegistration register(@RequestBody java.util.Map<String, Object> request) {
        String userId = (String) request.get("userId");
        Object eventIdObj = request.get("eventId");
        Long eventId;
        
        if (eventIdObj instanceof Integer) {
            eventId = ((Integer) eventIdObj).longValue();
        } else if (eventIdObj instanceof Long) {
            eventId = (Long) eventIdObj;
        } else if (eventIdObj instanceof String) {
            eventId = Long.parseLong((String) eventIdObj);
        } else {
            throw new RuntimeException("Invalid eventId format");
        }
        
        if (userId == null || userId.isBlank()) {
            throw new RuntimeException("userId is required");
        }
        
        return service.save(userId, eventId);
    }

    @PutMapping("/{id}/checkin")
    public EventRegistration checkIn(@PathVariable String id) {
        return service.checkIn(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
