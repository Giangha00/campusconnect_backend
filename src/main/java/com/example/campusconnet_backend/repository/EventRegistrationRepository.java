package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, String> {
    Optional<EventRegistration> findByUser_IdAndEvent_Id(String userId, Long eventId);
}
