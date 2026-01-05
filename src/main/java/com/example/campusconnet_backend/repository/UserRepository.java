package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> { }
