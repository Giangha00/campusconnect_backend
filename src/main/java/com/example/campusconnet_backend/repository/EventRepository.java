package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> { }
