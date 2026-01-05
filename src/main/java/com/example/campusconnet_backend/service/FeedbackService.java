package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Feedback;
import com.example.campusconnet_backend.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }

    public List<Feedback> getAll() {
        return repo.findAll();
    }

    public Feedback save(Feedback feedback) {
        return repo.save(feedback);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
