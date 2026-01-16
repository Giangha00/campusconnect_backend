package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.dto.EventCreateRequest;
import com.example.campusconnet_backend.dto.EventUpdateRequest;
import com.example.campusconnet_backend.entity.Admin;
import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.repository.AdminRepository;
import com.example.campusconnet_backend.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class EventService {
    private final EventRepository repo;
    private final AdminRepository adminRepo;

    public EventService(EventRepository repo, AdminRepository adminRepo) {
        this.repo = repo;
        this.adminRepo = adminRepo;
    }

    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return repo.findAllWithOrganizerAndDepartment();
    }

    @Transactional(readOnly = true)
    public Event findById(Long id) {
        return repo.findByIdWithOrganizerAndDepartment(id)
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));
    }

    @Transactional
    public Event create(EventCreateRequest request) {
        Instant now = Instant.now();
        
        Event event = new Event();
        
        // Load organizer if organizerId is provided
        if (request.getOrganizerId() != null && !request.getOrganizerId().isBlank()) {
            Admin organizer = adminRepo.findByIdWithDepartment(request.getOrganizerId())
                    .orElseThrow(() -> new RuntimeException("Admin not found: " + request.getOrganizerId()));
            event.setOrganizer(organizer);
        }
        
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setVenue(request.getVenue());
        event.setCategory(request.getCategory());
        event.setStatus(request.getStatus());
        event.setImageUrl(request.getImageUrl());
        event.setRegistrationRequired(request.getRegistrationRequired());
        event.setCapacity(request.getCapacity());
        event.setRegistrationStart(request.getRegistrationStart());
        event.setRegistrationEnd(request.getRegistrationEnd());
        event.setCreatedAt(now);
        event.setUpdatedAt(now);
        
        return repo.save(event);
    }

    @Transactional
    public Event update(Long id, EventUpdateRequest request) {
        Event event = findById(id);

        // Handle organizer update: if organizerId is provided and not blank, find and set it
        // If organizerId is explicitly null or empty, clear the organizer (set to null)
        if (request.getOrganizerId() != null) {
            if (request.getOrganizerId().isBlank()) {
                // Empty string means clear the organizer
                event.setOrganizer(null);
            } else {
                // Non-empty organizerId means find and set the organizer
                Admin organizer = adminRepo.findByIdWithDepartment(request.getOrganizerId())
                        .orElseThrow(() -> new RuntimeException("Admin not found: " + request.getOrganizerId()));
                event.setOrganizer(organizer);
            }
        }
        // If organizerId is null in request, don't change the existing organizer

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
            event.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            event.setEndDate(request.getEndDate());
        }
        if (request.getVenue() != null) {
            event.setVenue(request.getVenue());
        }
        if (request.getCategory() != null) {
            event.setCategory(request.getCategory());
        }
        if (request.getStatus() != null) {
            event.setStatus(request.getStatus());
        }
        if (request.getImageUrl() != null) {
            event.setImageUrl(request.getImageUrl());
        }
        if (request.getRegistrationRequired() != null) {
            event.setRegistrationRequired(request.getRegistrationRequired());
        }
        if (request.getCapacity() != null) {
            event.setCapacity(request.getCapacity());
        }
        if (request.getRegistrationStart() != null) {
            event.setRegistrationStart(request.getRegistrationStart());
        }
        if (request.getRegistrationEnd() != null) {
            event.setRegistrationEnd(request.getRegistrationEnd());
        }
        
        event.setUpdatedAt(Instant.now());

        return repo.save(event);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}