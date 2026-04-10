package com.studentmgmt.service;

import com.studentmgmt.dto.UserRegistrationDto;
import com.studentmgmt.entity.Role;
import com.studentmgmt.entity.User;
import com.studentmgmt.repository.RoleRepository;
import com.studentmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * USER SERVICE IMPLEMENTATION
 * ============================
 * Handles user registration with password encryption.
 *
 * IMPORTANT SECURITY NOTE:
 * ========================
 * Passwords are NEVER stored in plain text!
 * We use BCrypt to hash passwords before saving.
 * BCrypt is a one-way hash — you cannot reverse it.
 *
 * Example:
 *   Input:  "myPassword123"
 *   Stored: "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegistrationDto registrationDto) {
        log.info("Registering new user: {}", registrationDto.getEmail());

        // Create the User entity from DTO
        User user = User.builder()
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword())) // BCrypt hash!
                .build();

        // Assign the default role "ROLE_USER"
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");

        // If the role doesn't exist yet, create it
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
            log.info("Created default ROLE_USER");
        }

        roles.add(userRole);
        user.setRoles(roles);

        // Save user to database
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {} (id: {})", savedUser.getEmail(), savedUser.getId());

        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
