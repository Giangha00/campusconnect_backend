package com.example.campusconnet_backend.service;

import com.example.campusconnet_backend.entity.Admin;
import com.example.campusconnet_backend.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository repo;

    public AdminService(AdminRepository repo) {
        this.repo = repo;
    }

    public List<Admin> getAll() {
        return repo.findAll();
    }

    public Admin save(Admin admin) {
        return repo.save(admin);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
