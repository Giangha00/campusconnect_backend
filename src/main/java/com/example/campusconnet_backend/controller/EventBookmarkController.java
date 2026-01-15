package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.EventBookmarkResponse;
import com.example.campusconnet_backend.entity.EventBookmark;
import com.example.campusconnet_backend.service.EventBookmarkService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<EventBookmarkResponse> getAll(@RequestParam(required = false) String userId) {
        List<EventBookmark> bookmarks;
        if (userId != null && !userId.isBlank()) {
            bookmarks = service.getByUserId(userId);
        } else {
            bookmarks = service.getAll();
        }
        
        // Convert to DTO with userId and eventId
        return bookmarks.stream().map(bookmark -> {
            EventBookmarkResponse response = new EventBookmarkResponse();
            response.setId(bookmark.getId());
            response.setUserId(bookmark.getUser() != null ? bookmark.getUser().getId() : null);
            response.setEventId(bookmark.getEvent() != null ? bookmark.getEvent().getId() : null);
            response.setCreatedAt(bookmark.getCreatedAt());
            return response;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public EventBookmarkResponse create(@RequestBody java.util.Map<String, Object> request) {
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
        
        EventBookmark bookmark = service.save(userId, eventId);
        
        // Convert to DTO
        EventBookmarkResponse response = new EventBookmarkResponse();
        response.setId(bookmark.getId());
        response.setUserId(bookmark.getUser() != null ? bookmark.getUser().getId() : userId);
        response.setEventId(bookmark.getEvent() != null ? bookmark.getEvent().getId() : eventId);
        response.setCreatedAt(bookmark.getCreatedAt());
        return response;
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
