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
    public EventBookmark create(@RequestBody EventBookmark bookmark) {
        return service.save(bookmark);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
