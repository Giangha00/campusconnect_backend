package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
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
    public Event create(@RequestBody Event e) {
        return service.create(e);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event e) {
        return service.update(id, e);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
