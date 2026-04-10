package com.studentmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MAIN APPLICATION CLASS
 * =======================
 * This is the entry point of the entire application.
 * 
 * @SpringBootApplication combines three annotations:
 *   1. @Configuration    — Marks this as a configuration class
 *   2. @EnableAutoConfiguration — Spring Boot auto-configures beans
 *   3. @ComponentScan    — Scans for components in this package and sub-packages
 *
 * When you run this class, Spring Boot:
 *   1. Starts an embedded Tomcat server on port 8080
 *   2. Scans all packages for @Controller, @Service, @Repository, @Component
 *   3. Creates and wires all beans together (Dependency Injection)
 *   4. Connects to the database
 *   5. Your app is ready!
 */
@SpringBootApplication
public class StudentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }
}
