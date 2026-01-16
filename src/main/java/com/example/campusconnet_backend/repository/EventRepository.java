package com.example.campusconnet_backend.repository;

import com.example.campusconnet_backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.organizer o LEFT JOIN FETCH o.departmentEntity")
    List<Event> findAllWithOrganizerAndDepartment();
    
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.organizer o LEFT JOIN FETCH o.departmentEntity WHERE e.id = :id")
    Optional<Event> findByIdWithOrganizerAndDepartment(Long id);
}
