package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.Gallery;
import com.example.campusconnet_backend.repository.GalleryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/galleries")
@CrossOrigin(origins = "*")
public class GalleryController {

    private final GalleryRepository repo;

    public GalleryController(GalleryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Gallery> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Gallery getById(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery not found: " + id));
    }

    @PostMapping
    public Gallery create(@RequestBody Gallery gallery) {
        return repo.save(gallery);
    }

    @PutMapping("/{id}")
    public Gallery update(@PathVariable Long id, @RequestBody Gallery data) {
        Gallery g = getById(id);

        g.setImageUrl(data.getImageUrl());
        g.setAltText(data.getAltText());
        g.setCategory(data.getCategory());
        g.setEventName(data.getEventName());
        g.setYear(data.getYear());
        g.setDate(data.getDate());

        return repo.save(g);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
