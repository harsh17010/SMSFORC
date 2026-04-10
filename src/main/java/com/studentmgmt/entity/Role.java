package com.studentmgmt.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ROLE ENTITY
 * ============
 * Represents a user role (e.g., ADMIN, USER).
 * Roles control what a user can and cannot do in the application.
 *
 * RELATIONSHIP:
 * - A Role can be assigned to MANY Users (Many-to-Many)
 * - This is the "other side" of the relationship defined in User entity
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // e.g., "ROLE_ADMIN", "ROLE_USER"

    /**
     * Constructor with just the role name.
     * Convenient for creating roles quickly.
     */
    public Role(String name) {
        this.name = name;
    }
}
