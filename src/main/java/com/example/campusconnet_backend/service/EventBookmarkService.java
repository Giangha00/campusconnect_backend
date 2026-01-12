package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.EventBookmark;
import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.repository.EventBookmarkRepository;
import com.example.campusconnet_backend.repository.EventRepository;
import com.example.campusconnet_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventBookmarkService {

    private final EventBookmarkRepository repo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    public EventBookmarkService(EventBookmarkRepository repo, UserRepository userRepo, EventRepository eventRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    @Transactional(readOnly = true)
    public List<EventBookmark> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<EventBookmark> getByUserId(String userId) {
        return repo.findByUser_Id(userId);
    }

    @Transactional
    public EventBookmark save(EventBookmark bookmark) {
        // Nếu bookmark đã có user và event objects, dùng trực tiếp
        if (bookmark.getUser() != null && bookmark.getEvent() != null) {
            if (bookmark.getId() == null) {
                bookmark.setId(UUID.randomUUID().toString());
            }
            if (bookmark.getCreatedAt() == null) {
                bookmark.setCreatedAt(Instant.now());
            }
            return repo.save(bookmark);
        }

        // Nếu không có, cần load từ database (sẽ được xử lý trong controller)
        throw new RuntimeException("User and Event must be provided");
    }

    @Transactional
    public EventBookmark save(String userId, Long eventId) {
        // Kiểm tra xem đã tồn tại chưa
        Optional<EventBookmark> existing = repo.findByUser_IdAndEvent_Id(userId, eventId);

        if (existing.isPresent()) {
            throw new RuntimeException("Bookmark already exists for this user and event");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        EventBookmark bookmark = new EventBookmark();
        bookmark.setId(UUID.randomUUID().toString());
        bookmark.setUser(user);
        bookmark.setEvent(event);
        bookmark.setCreatedAt(Instant.now());

        return repo.save(bookmark);
    }

    @Transactional
    public void delete(String id) {
        repo.deleteById(id);
    }
}
