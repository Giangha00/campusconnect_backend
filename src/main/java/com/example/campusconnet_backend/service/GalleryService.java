package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Gallery;
import com.example.campusconnet_backend.repository.GalleryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryService {

    private final GalleryRepository repo;

    public GalleryService(GalleryRepository repo) {
        this.repo = repo;
    }

    public List<Gallery> getAll() {
        return repo.findAll();
    }

    public Gallery save(Gallery gallery) {
        return repo.save(gallery);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
