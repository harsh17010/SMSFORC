package com.studentmgmt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * USER ENTITY
 * ============
 * Represents a registered user in the system.
 * Users can log in, and their roles determine access levels.
 *
 * RELATIONSHIPS:
 * - A User can have MANY Roles → @ManyToMany
 * - The join table "users_roles" links users and roles together
 *
 * IMPORTANT: We store passwords ENCRYPTED (using BCrypt).
 *            NEVER store plain-text passwords in a real application!
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password; // Stored as BCrypt hash

    /**
     * MANY-TO-MANY RELATIONSHIP with Role
     * =====================================
     * - fetch = EAGER → Roles are loaded immediately with the user
     *                    (needed for Spring Security to check permissions)
     * - cascade = ALL → When a user is saved/deleted, cascade the operation to roles
     * - @JoinTable    → Creates a linking table called "users_roles"
     *   - joinColumns         → Foreign key pointing to the users table
     *   - inverseJoinColumns  → Foreign key pointing to the roles table
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
