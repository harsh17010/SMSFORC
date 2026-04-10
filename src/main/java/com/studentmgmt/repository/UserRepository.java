package com.studentmgmt.repository;

import com.studentmgmt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * USER REPOSITORY
 * ================
 * Handles database operations for the User entity.
 * Spring Data JPA auto-generates the implementation.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     * Used during login to look up the user.
     *
     * @param email the email to search for
     * @return the User if found, null otherwise
     */
    User findByEmail(String email);

    /**
     * Check if a user with the given email already exists.
     * Used during registration to prevent duplicate accounts.
     */
    boolean existsByEmail(String email);
}
