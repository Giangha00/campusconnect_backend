package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query("SELECT a FROM Admin a LEFT JOIN FETCH a.departmentEntity")
    List<Admin> findAllWithDepartment();
    
    @Query("SELECT a FROM Admin a LEFT JOIN FETCH a.departmentEntity WHERE a.id = :id")
    Optional<Admin> findByIdWithDepartment(@Param("id") String id);
    
    @Query("SELECT a FROM Admin a LEFT JOIN FETCH a.departmentEntity WHERE a.username = :username")
    Optional<Admin> findByUsernameWithDepartment(@Param("username") String username);
}
