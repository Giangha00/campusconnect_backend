package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.EventRegistration;
import com.example.campusconnet_backend.repository.EventRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository repo;

    public EventRegistrationService(EventRegistrationRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<EventRegistration> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public EventRegistration findById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    @Transactional
    public EventRegistration save(EventRegistration reg) {
        Instant now = Instant.now();
        
        if (reg.getId() == null) {
            reg.setId(UUID.randomUUID().toString());
        }
        
        if (reg.getRegistrationDate() == null) {
            reg.setRegistrationDate(now);
        }
        
        if (reg.getCheckedIn() == null) {
            reg.setCheckedIn(false);
        }
        
        if (reg.getCreatedAt() == null) {
            reg.setCreatedAt(now);
        }
        reg.setUpdatedAt(now);
        
        return repo.save(reg);
    }

    @Transactional
    public EventRegistration checkIn(String id) {
        EventRegistration reg = findById(id);
        reg.setCheckedIn(true);
        reg.setCheckedInAt(Instant.now());
        reg.setUpdatedAt(Instant.now());
        return repo.save(reg);
    }

    @Transactional
    public void delete(String id) {
        repo.deleteById(id);
    }
}
