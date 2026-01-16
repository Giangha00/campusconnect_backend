package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.departmentEntity")
    List<User> findAllWithDepartment();
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.departmentEntity WHERE u.id = :id")
    Optional<User> findByIdWithDepartment(@Param("id") String id);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.departmentEntity WHERE u.username = :username")
    Optional<User> findByUsernameWithDepartment(@Param("username") String username);
}
