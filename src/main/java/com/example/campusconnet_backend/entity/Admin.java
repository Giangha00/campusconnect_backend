package com.example.campusconnet_backend.entity;

import com.example.campusconnet_backend.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role; // admin / faculty

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_admins_department"))
    @JsonIgnore // Prevent Jackson from serializing this lazy-loaded relationship
    private Department departmentEntity;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;
}