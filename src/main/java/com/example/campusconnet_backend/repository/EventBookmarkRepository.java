package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.EventBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventBookmarkRepository extends JpaRepository<EventBookmark, String> {
    List<EventBookmark> findByUser_Id(String userId);
    Optional<EventBookmark> findByUser_IdAndEvent_Id(String userId, Long eventId);
}
