package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.EventRegistration;
import com.example.campusconnet_backend.repository.EventRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class EventRegistrationController {

    private final EventRegistrationRepository repo;

    public EventRegistrationController(EventRegistrationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<EventRegistration> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public EventRegistration register(@RequestBody EventRegistration reg) {
        if (reg.getId() == null) {
            reg.setId(UUID.randomUUID().toString());
        }
        return repo.save(reg);
    }

    @PutMapping("/{id}/checkin")
    public EventRegistration checkIn(@PathVariable String id) {
        EventRegistration reg = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        reg.setCheckedIn(true);
        reg.setCheckedInAt(java.time.Instant.now());

        return repo.save(reg);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}
