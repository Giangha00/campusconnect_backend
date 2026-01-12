package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.EventBookmark;
import com.example.campusconnet_backend.service.EventBookmarkService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-bookmarks")
@CrossOrigin(origins = "*")
@Transactional
public class EventBookmarkController {

    private final EventBookmarkService service;

    public EventBookmarkController(EventBookmarkService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventBookmark> getAll(@RequestParam(required = false) String userId) {
        if (userId != null && !userId.isBlank()) {
            return service.getByUserId(userId);
        }
        return service.getAll();
    }

    @PostMapping
    public EventBookmark create(@RequestBody java.util.Map<String, Object> request) {
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
