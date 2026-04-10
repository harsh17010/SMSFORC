package com.studentmgmt.config;

import com.studentmgmt.entity.Role;
import com.studentmgmt.entity.Student;
import com.studentmgmt.entity.User;
import com.studentmgmt.repository.RoleRepository;
import com.studentmgmt.repository.StudentRepository;
import com.studentmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * DATA INITIALIZER
 * ==================
 * This class runs ONCE when the application starts.
 * It creates:
 * - Default roles (ROLE_USER, ROLE_ADMIN)
 * - A default admin user
 * - Sample student data for testing
 *
 * CommandLineRunner is a Spring Boot callback that runs after
 * the application context is fully initialized.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            log.info("=== Initializing Sample Data ===");

            // ===================== CREATE ROLES =====================
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
                log.info("Created ROLE_ADMIN");
            }

            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole == null) {
                userRole = roleRepository.save(new Role("ROLE_USER"));
                log.info("Created ROLE_USER");
            }

            // ===================== CREATE DEFAULT ADMIN =====================
            if (!userRepository.existsByEmail("admin@admin.com")) {
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                adminRoles.add(userRole);

                User admin = User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@admin.com")
                        .password(passwordEncoder.encode("admin123"))
                        .roles(adminRoles)
                        .build();

                userRepository.save(admin);
                log.info("Created default admin user: admin@admin.com / admin123");
            }

            // ===================== CREATE DEFAULT USER =====================
            if (!userRepository.existsByEmail("user@user.com")) {
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(userRole);

                User normalUser = User.builder()
                        .firstName("Normal")
                        .lastName("User")
                        .email("user@user.com")
                        .password(passwordEncoder.encode("user123"))
                        .roles(userRoles)
                        .build();

                userRepository.save(normalUser);
                log.info("Created default user: user@user.com / user123");
            }

            // ===================== CREATE SAMPLE STUDENTS =====================
            if (studentRepository.count() == 0) {
                studentRepository.save(Student.builder()
                        .firstName("Rahul").lastName("Sharma")
                        .email("rahul.sharma@email.com")
                        .department("Computer Science").phone("9876543210")
                        .address("Mumbai, Maharashtra").build());

                studentRepository.save(Student.builder()
                        .firstName("Priya").lastName("Patel")
                        .email("priya.patel@email.com")
                        .department("Electronics").phone("9876543211")
                        .address("Delhi, India").build());

                studentRepository.save(Student.builder()
                        .firstName("Amit").lastName("Kumar")
                        .email("amit.kumar@email.com")
                        .department("Mechanical").phone("9876543212")
                        .address("Bangalore, Karnataka").build());

                studentRepository.save(Student.builder()
                        .firstName("Sneha").lastName("Reddy")
                        .email("sneha.reddy@email.com")
                        .department("Computer Science").phone("9876543213")
                        .address("Hyderabad, Telangana").build());

                studentRepository.save(Student.builder()
                        .firstName("Vikram").lastName("Singh")
                        .email("vikram.singh@email.com")
                        .department("Civil Engineering").phone("9876543214")
                        .address("Jaipur, Rajasthan").build());

                studentRepository.save(Student.builder()
                        .firstName("Ananya").lastName("Gupta")
                        .email("ananya.gupta@email.com")
                        .department("Information Technology").phone("9876543215")
                        .address("Pune, Maharashtra").build());

                studentRepository.save(Student.builder()
                        .firstName("Rohan").lastName("Mehta")
                        .email("rohan.mehta@email.com")
                        .department("Electrical").phone("9876543216")
                        .address("Chennai, Tamil Nadu").build());

                studentRepository.save(Student.builder()
                        .firstName("Kavya").lastName("Nair")
                        .email("kavya.nair@email.com")
                        .department("Computer Science").phone("9876543217")
                        .address("Kochi, Kerala").build());

                studentRepository.save(Student.builder()
                        .firstName("Arjun").lastName("Verma")
                        .email("arjun.verma@email.com")
                        .department("Mechanical").phone("9876543218")
                        .address("Lucknow, Uttar Pradesh").build());

                studentRepository.save(Student.builder()
                        .firstName("Ishita").lastName("Joshi")
                        .email("ishita.joshi@email.com")
                        .department("Biotechnology").phone("9876543219")
                        .address("Ahmedabad, Gujarat").build());

                log.info("Created 10 sample students");
            }

            log.info("=== Sample Data Initialization Complete ===");
            log.info("=== Login Credentials ===");
            log.info("Admin: admin@admin.com / admin123");
            log.info("User:  user@user.com / user123");
        };
    }
}
