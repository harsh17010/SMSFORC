package com.studentmgmt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * STUDENT ENTITY
 * ===============
 * This class maps to the "students" table in the database.
 * Each instance of this class represents ONE ROW in the table.
 *
 * HOW IT WORKS:
 * - @Entity tells Hibernate "this is a database table"
 * - @Table(name = "students") specifies the table name
 * - @Id marks the primary key
 * - @GeneratedValue auto-generates the ID (1, 2, 3, ...)
 * - @Column maps each field to a table column
 *
 * LOMBOK ANNOTATIONS (saves you from writing boilerplate code):
 * - @Getter    → Auto-generates all getter methods
 * - @Setter    → Auto-generates all setter methods
 * - @NoArgsConstructor  → Generates no-argument constructor
 * - @AllArgsConstructor → Generates constructor with all fields
 * - @Builder   → Lets you create objects with builder pattern
 */
@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 100)
    private String department;

    @Column(length = 20)
    private String phone;

    @Column(length = 200)
    private String address;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * @PrePersist runs BEFORE a new entity is saved to the database.
     * We use it to auto-set the created timestamp.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * @PreUpdate runs BEFORE an existing entity is updated.
     * We use it to auto-update the modified timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
