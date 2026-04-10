package com.studentmgmt.service;

import com.studentmgmt.dto.UserRegistrationDto;
import com.studentmgmt.entity.User;

/**
 * USER SERVICE INTERFACE
 * =======================
 * Defines operations for user management (registration).
 */
public interface UserService {

    /**
     * Register a new user.
     *
     * @param registrationDto the registration form data
     * @return the saved User entity
     */
    User registerUser(UserRegistrationDto registrationDto);

    /**
     * Check if user already exists with given email.
     *
     * @param email the email to check
     * @return true if user exists
     */
    boolean emailExists(String email);
}
