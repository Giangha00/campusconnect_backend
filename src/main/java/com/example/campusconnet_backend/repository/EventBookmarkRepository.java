package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.EventBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventBookmarkRepository extends JpaRepository<EventBookmark, String> {
    List<EventBookmark> findByUser_Id(String userId);
}
