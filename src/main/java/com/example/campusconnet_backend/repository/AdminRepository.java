package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
