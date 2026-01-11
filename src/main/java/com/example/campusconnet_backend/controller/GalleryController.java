package com.example.campusconnet_backend.controller;

import com.example.campusconnet_backend.entity.Gallery;
import com.example.campusconnet_backend.service.GalleryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@CrossOrigin(origins = "*")
@Transactional
public class GalleryController {

    private final GalleryService service;

    public GalleryController(GalleryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Gallery> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Gallery getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Gallery create(@RequestBody Gallery gallery) {
        return service.save(gallery);
    }

    @PutMapping("/{id}")
    public Gallery update(@PathVariable Long id, @RequestBody Gallery data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
