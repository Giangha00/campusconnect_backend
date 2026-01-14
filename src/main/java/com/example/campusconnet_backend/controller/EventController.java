package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.EventCreateRequest;
import com.example.campusconnet_backend.dto.EventUpdateRequest;
import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.service.EventService;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
@Transactional
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<Event> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Event getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Event create(@Valid @RequestBody EventCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @Valid @RequestBody EventUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
