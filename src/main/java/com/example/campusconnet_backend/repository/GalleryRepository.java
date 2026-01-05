package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
}
