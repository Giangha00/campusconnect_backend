package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.Feedback;
import com.example.campusconnet_backend.repository.FeedbackRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin(origins = "*")
public class FeedbackController {

    private final FeedbackRepository repo;

    public FeedbackController(FeedbackRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Feedback> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Feedback getById(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found: " + id));
    }

    @PostMapping
    public Feedback create(@RequestBody Feedback feedback) {
        return repo.save(feedback);
    }

    @PutMapping("/{id}")
    public Feedback update(@PathVariable Long id, @RequestBody Feedback data) {
        Feedback fb = getById(id);

        fb.setName(data.getName());
        fb.setEmail(data.getEmail());
        fb.setRating(data.getRating());
        fb.setFeedback(data.getFeedback());
        fb.setStatus(data.getStatus());

        return repo.save(fb);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
