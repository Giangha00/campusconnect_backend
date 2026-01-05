package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.EventRegistration;
import com.example.campusconnet_backend.repository.EventRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository repo;

    public EventRegistrationService(EventRegistrationRepository repo) {
        this.repo = repo;
    }

    public List<EventRegistration> getAll() {
        return repo.findAll();
    }

    public EventRegistration save(EventRegistration reg) {
        return repo.save(reg);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
