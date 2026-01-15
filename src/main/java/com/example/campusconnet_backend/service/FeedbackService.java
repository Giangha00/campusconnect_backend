package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Feedback;
import com.example.campusconnet_backend.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Feedback> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Feedback findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Feedback> getByEventId(Long eventId) {
        return repo.findByEvent_Id(eventId);
    }

    @Transactional
    public Feedback save(Feedback feedback) {
        Instant now = Instant.now();
        if (feedback.getCreatedAt() == null) {
            feedback.setCreatedAt(now);
        }
        feedback.setUpdatedAt(now);
        return repo.save(feedback);
    }

    @Transactional
    public Feedback update(Long id, Feedback data) {
        Feedback fb = findById(id);

        fb.setName(data.getName());
        fb.setEmail(data.getEmail());
        fb.setRating(data.getRating());
        fb.setFeedback(data.getFeedback());
        fb.setStatus(data.getStatus());
        fb.setUpdatedAt(Instant.now());

        return repo.save(fb);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
