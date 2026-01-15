package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.dto.FeedbackCreateRequest;
import com.example.campusconnet_backend.entity.Feedback;
import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.model.FeedbackStatus;
import com.example.campusconnet_backend.repository.FeedbackRepository;
import com.example.campusconnet_backend.repository.UserRepository;
import com.example.campusconnet_backend.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public FeedbackService(FeedbackRepository repo, UserRepository userRepository, EventRepository eventRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
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
    public Feedback create(FeedbackCreateRequest request) {
        Feedback feedback = new Feedback();
        
        // Map user if userId is provided
        if (request.getUserId() != null && !request.getUserId().isEmpty()) {
            User user = userRepository.findById(request.getUserId())
                    .orElse(null);
            feedback.setUser(user);
        }
        
        // Map event if eventId is provided
        if (request.getEventId() != null) {
            Event event = eventRepository.findById(request.getEventId())
                    .orElse(null);
            feedback.setEvent(event);
        }
        
        // Set other fields
        feedback.setName(request.getName());
        feedback.setEmail(request.getEmail());
        feedback.setUserType(request.getUserType());
        feedback.setRating(request.getRating());
        feedback.setFeedback(request.getFeedback());
        
        // Set status, default to active if not provided
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            try {
                feedback.setStatus(FeedbackStatus.valueOf(request.getStatus().toLowerCase()));
            } catch (IllegalArgumentException e) {
                feedback.setStatus(FeedbackStatus.active);
            }
        } else {
            feedback.setStatus(FeedbackStatus.active);
        }
        
        // Set timestamps
        Instant now = Instant.now();
        feedback.setCreatedAt(now);
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
