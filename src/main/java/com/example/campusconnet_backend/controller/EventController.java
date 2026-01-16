package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.EventCreateRequest;
import com.example.campusconnet_backend.dto.EventResponse;
import com.example.campusconnet_backend.dto.EventUpdateRequest;
import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.service.EventService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventResponse> getAll() {
        return service.findAllResponses();
    }

    @GetMapping("/{id}")
    public EventResponse getById(@PathVariable Long id) {
        return service.findResponseById(id);
    }

    @PostMapping
    public EventResponse create(@Valid @RequestBody EventCreateRequest request) {
        Event event = service.create(request);
        return service.findResponseById(event.getId());
    }

    @PutMapping("/{id}")
    public EventResponse update(@PathVariable Long id, @Valid @RequestBody EventUpdateRequest request) {
        Event event = service.update(id, request);
        return service.findResponseById(event.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
