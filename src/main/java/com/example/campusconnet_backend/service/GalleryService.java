package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Gallery;
import com.example.campusconnet_backend.repository.GalleryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GalleryService {

    private final GalleryRepository repo;

    public GalleryService(GalleryRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Gallery> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Gallery findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery not found: " + id));
    }

    @Transactional
    public Gallery save(Gallery gallery) {
        return repo.save(gallery);
    }

    @Transactional
    public Gallery update(Long id, Gallery data) {
        Gallery g = findById(id);

        g.setImageUrl(data.getImageUrl());
        g.setAltText(data.getAltText());
        g.setCategory(data.getCategory());
        g.setEventName(data.getEventName());
        g.setYear(data.getYear());
        g.setDate(data.getDate());

        return repo.save(g);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
