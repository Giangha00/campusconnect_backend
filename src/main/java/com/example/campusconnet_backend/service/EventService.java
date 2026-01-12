package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class EventService {
    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Event findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found: " + id));
    }

    @Transactional
    public Event create(Event e) {
        Instant now = Instant.now();
        if (e.getCreatedAt() == null) {
            e.setCreatedAt(now);
        }
        e.setUpdatedAt(now);
        return repo.save(e);
    }

    @Transactional
    public Event update(Long id, Event data) {
        Event e = findById(id);

        e.setTitle(data.getTitle());
        e.setDescription(data.getDescription());
        e.setStartDate(data.getStartDate());
        e.setEndDate(data.getEndDate());
        e.setVenue(data.getVenue());
        e.setCategory(data.getCategory());
        e.setStatus(data.getStatus());
        e.setImageUrl(data.getImageUrl());
        e.setRegistrationRequired(data.getRegistrationRequired());
        e.setCapacity(data.getCapacity());
        e.setRegistrationStart(data.getRegistrationStart());
        e.setRegistrationEnd(data.getRegistrationEnd());
        e.setOrganizer(data.getOrganizer());
        e.setUpdatedAt(Instant.now());

        return repo.save(e);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}