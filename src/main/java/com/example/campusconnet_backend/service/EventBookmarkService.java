package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.EventBookmark;
import com.example.campusconnet_backend.repository.EventBookmarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EventBookmarkService {

    private final EventBookmarkRepository repo;

    public EventBookmarkService(EventBookmarkRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<EventBookmark> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<EventBookmark> getByUserId(String userId) {
        return repo.findByUser_Id(userId);
    }

    @Transactional
    public EventBookmark save(EventBookmark bookmark) {
        if (bookmark.getId() == null) {
            bookmark.setId(UUID.randomUUID().toString());
        }
        if (bookmark.getCreatedAt() == null) {
            bookmark.setCreatedAt(Instant.now());
        }
        return repo.save(bookmark);
    }

    @Transactional
    public void delete(String id) {
        repo.deleteById(id);
    }
}
