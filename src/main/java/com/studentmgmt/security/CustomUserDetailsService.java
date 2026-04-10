package com.studentmgmt.security;

import com.studentmgmt.entity.User;
import com.studentmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * CUSTOM USER DETAILS SERVICE
 * =============================
 * Spring Security needs to know HOW to load user data from your database.
 * This class bridges YOUR user entity with Spring Security's authentication system.
 *
 * HOW SPRING SECURITY LOGIN WORKS:
 * =================================
 * 1. User submits login form with email + password
 * 2. Spring Security calls loadUserByUsername(email)
 * 3. This method fetches the user from the database
 * 4. Spring Security compares the submitted password with the stored BCrypt hash
 * 5. If they match → Login successful! If not → Error message shown.
 *
 * The UserDetails object returned contains:
 * - Username (email in our case)
 * - Encrypted password
 * - List of roles/authorities (ROLE_USER, ROLE_ADMIN)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Load user by their email address (we use email as the "username").
     * Spring Security calls this method during the login process.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);

        // Find user in database
        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        log.debug("User found: {} with {} roles", email, user.getRoles().size());

        // Convert our User entity to Spring Security's UserDetails
        // org.springframework.security.core.userdetails.User is Spring's built-in class
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user)
        );
    }

    /**
     * Convert our Role entities to Spring Security's GrantedAuthority objects.
     * Spring Security uses these to check if a user has permission to access a resource.
     *
     * Example: Role(name="ROLE_ADMIN") → SimpleGrantedAuthority("ROLE_ADMIN")
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
