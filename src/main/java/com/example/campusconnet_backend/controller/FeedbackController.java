package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.dto.FeedbackCreateRequest;
import com.example.campusconnet_backend.entity.Feedback;
import com.example.campusconnet_backend.service.FeedbackService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
@Transactional
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @GetMapping
    public List<Feedback> getAll(@RequestParam(required = false) Long eventId) {
        if (eventId != null) {
            return service.getByEventId(eventId);
        }
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Feedback getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Feedback create(@RequestBody FeedbackCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Feedback update(@PathVariable Long id, @RequestBody Feedback data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
