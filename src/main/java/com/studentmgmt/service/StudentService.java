package com.studentmgmt.service;

import com.studentmgmt.dto.StudentDto;
import org.springframework.data.domain.Page;

/**
 * STUDENT SERVICE INTERFACE
 * ==========================
 * This interface defines WHAT the service can do (the contract).
 * The actual HOW is in StudentServiceImpl.
 *
 * WHY USE AN INTERFACE?
 * ---------------------
 * 1. LOOSE COUPLING: Controllers depend on the interface, not the implementation.
 *    → You can swap implementations without changing controllers.
 * 2. TESTABILITY: You can mock this interface in unit tests.
 * 3. CLEAN ARCHITECTURE: Separates "what" from "how".
 */
public interface StudentService {

    /**
     * Get a paginated list of all students.
     *
     * @param pageNo   the page number (0-based)
     * @param pageSize number of records per page
     * @param sortField the field to sort by
     * @param sortDirection ascending or descending
     * @return a Page of StudentDto objects
     */
    Page<StudentDto> getAllStudents(int pageNo, int pageSize, String sortField, String sortDirection);

    /**
     * Get a single student by ID.
     *
     * @param id the student's ID
     * @return the StudentDto
     * @throws ResourceNotFoundException if student not found
     */
    StudentDto getStudentById(Long id);

    /**
     * Create a new student.
     *
     * @param studentDto the student data
     * @return the saved StudentDto (with generated ID)
     */
    StudentDto createStudent(StudentDto studentDto);

    /**
     * Update an existing student.
     *
     * @param id the student's ID
     * @param studentDto the updated data
     * @return the updated StudentDto
     */
    StudentDto updateStudent(Long id, StudentDto studentDto);

    /**
     * Delete a student by ID.
     *
     * @param id the student's ID
     */
    void deleteStudent(Long id);

    /**
     * Search students by keyword (searches name, email, department).
     *
     * @param keyword the search term
     * @param pageNo page number
     * @param pageSize records per page
     * @param sortField sort field
     * @param sortDirection sort direction
     * @return a Page of matching students
     */
    Page<StudentDto> searchStudents(String keyword, int pageNo, int pageSize,
                                     String sortField, String sortDirection);
}
