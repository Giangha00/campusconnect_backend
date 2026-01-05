package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.EventBookmark;
import com.example.campusconnet_backend.repository.EventBookmarkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventBookmarkService {

    private final EventBookmarkRepository repo;

    public EventBookmarkService(EventBookmarkRepository repo) {
        this.repo = repo;
    }

    public List<EventBookmark> getAll() {
        return repo.findAll();
    }

    public EventBookmark save(EventBookmark bookmark) {
        return repo.save(bookmark);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
