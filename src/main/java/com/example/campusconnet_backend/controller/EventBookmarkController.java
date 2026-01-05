package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.EventBookmark;
import com.example.campusconnet_backend.repository.EventBookmarkRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
@CrossOrigin(origins = "*")
public class EventBookmarkController {

    private final EventBookmarkRepository repo;

    public EventBookmarkController(EventBookmarkRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<EventBookmark> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public EventBookmark create(@RequestBody EventBookmark bookmark) {
        if (bookmark.getId() == null) {
            bookmark.setId(UUID.randomUUID().toString());
        }
        return repo.save(bookmark);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}
