package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, String> {
}
