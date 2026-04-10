package com.studentmgmt.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * STUDENT DTO (Data Transfer Object)
 * ====================================
 * A DTO is a "carrier" object that moves data between layers.
 *
 * WHY USE DTOs INSTEAD OF ENTITIES?
 * ----------------------------------
 * 1. SECURITY:   We don't expose internal entity fields (like IDs, timestamps)
 * 2. VALIDATION: We define validation rules here, not on the entity
 * 3. FLEXIBILITY: We can shape the data differently for different use cases
 * 4. DECOUPLING: Frontend changes don't affect the database layer
 *
 * VALIDATION ANNOTATIONS:
 * - @NotBlank → Field cannot be null, empty, or whitespace
 * - @Email    → Must be a valid email format
 * - @Size     → String length constraints
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {

    private Long id; // Used for updates, null for new students

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Size(max = 100, message = "Department name cannot exceed 100 characters")
    private String department;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;
}
