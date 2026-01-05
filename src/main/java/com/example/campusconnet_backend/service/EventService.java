package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public List<Event> findAll() { return repo.findAll(); }

    public Event findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found: " + id));
    }

    public Event create(Event e) { return repo.save(e); }

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

        return repo.save(e);
    }

    public void delete(Long id) { repo.deleteById(id); }
}
