package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.EventRegistration;
import com.example.campusconnet_backend.entity.Event;
import com.example.campusconnet_backend.entity.User;
import com.example.campusconnet_backend.repository.EventRegistrationRepository;
import com.example.campusconnet_backend.repository.EventRepository;
import com.example.campusconnet_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository repo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    public EventRegistrationService(EventRegistrationRepository repo, UserRepository userRepo,
            EventRepository eventRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    @Transactional(readOnly = true)
    public List<EventRegistration> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public EventRegistration findById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    @Transactional
    public EventRegistration save(EventRegistration reg) {
        // Nếu reg đã có user và event objects, dùng trực tiếp
        if (reg.getUser() != null && reg.getEvent() != null) {
            Instant now = Instant.now();

            if (reg.getId() == null) {
                reg.setId(UUID.randomUUID().toString());
            }

            // Tự động generate ticketNumber nếu không có
            if (reg.getTicketNumber() == null || reg.getTicketNumber().isBlank()) {
                String ticketNumber = generateTicketNumber(reg.getId(), now);
                reg.setTicketNumber(ticketNumber);
            }

            if (reg.getRegistrationDate() == null) {
                reg.setRegistrationDate(now);
            }

            if (reg.getCheckedIn() == null) {
                reg.setCheckedIn(false);
            }

            if (reg.getCreatedAt() == null) {
                reg.setCreatedAt(now);
            }
            reg.setUpdatedAt(now);

            return repo.save(reg);
        }

        // Nếu không có, cần load từ database (sẽ được xử lý trong controller)
        throw new RuntimeException("User and Event must be provided");
    }

    @Transactional
    public EventRegistration save(String userId, Long eventId) {
        // Kiểm tra xem đã đăng ký chưa
        java.util.Optional<EventRegistration> existing = repo.findByUser_IdAndEvent_Id(userId, eventId);

        if (existing.isPresent()) {
            throw new RuntimeException("Registration already exists for this user and event");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        Instant now = Instant.now();
        String id = UUID.randomUUID().toString();
        String ticketNumber = generateTicketNumber(id, now);

        EventRegistration reg = new EventRegistration();
        reg.setId(id);
        reg.setUser(user);
        reg.setEvent(event);
        reg.setTicketNumber(ticketNumber);
        reg.setRegistrationDate(now);
        reg.setCheckedIn(false);
        reg.setCreatedAt(now);
        reg.setUpdatedAt(now);

        return repo.save(reg);
    }

    private String generateTicketNumber(String id, Instant timestamp) {
        // Format: TICKET-YYYYMMDD-HHMMSS-XXXX (4 ký tự cuối của UUID)
        ZonedDateTime zonedDateTime = timestamp.atZone(ZoneId.systemDefault());

        String year = String.valueOf(zonedDateTime.getYear());
        String month = String.format("%02d", zonedDateTime.getMonthValue());
        String day = String.format("%02d", zonedDateTime.getDayOfMonth());
        String hour = String.format("%02d", zonedDateTime.getHour());
        String minute = String.format("%02d", zonedDateTime.getMinute());
        String second = String.format("%02d", zonedDateTime.getSecond());

        // Lấy 4 ký tự cuối của UUID (bỏ dấu gạch ngang)
        String uuidWithoutDashes = id.replace("-", "");
        String uuidSuffix = uuidWithoutDashes.substring(Math.max(0, uuidWithoutDashes.length() - 4)).toUpperCase();

        return String.format("TICKET-%s%s%s-%s%s%s-%s", year, month, day, hour, minute, second, uuidSuffix);
    }

    @Transactional
    public EventRegistration checkIn(String id) {
        EventRegistration reg = findById(id);
        reg.setCheckedIn(true);
        reg.setCheckedInAt(Instant.now());
        reg.setUpdatedAt(Instant.now());
        return repo.save(reg);
    }

    @Transactional
    public void delete(String id) {
        repo.deleteById(id);
    }
}
