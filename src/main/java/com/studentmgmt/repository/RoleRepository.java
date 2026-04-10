package com.studentmgmt.repository;

import com.studentmgmt.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ROLE REPOSITORY
 * ================
 * Handles database operations for the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by its name (e.g., "ROLE_USER", "ROLE_ADMIN").
     *
     * @param name the role name
     * @return the Role if found, null otherwise
     */
    Role findByName(String name);
}
