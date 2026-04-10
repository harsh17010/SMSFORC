package com.studentmgmt.service;

import com.studentmgmt.dto.StudentDto;
import com.studentmgmt.entity.Student;
import com.studentmgmt.exception.ResourceNotFoundException;
import com.studentmgmt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * STUDENT SERVICE IMPLEMENTATION
 * ===============================
 * This class contains the actual BUSINESS LOGIC for student operations.
 *
 * ANNOTATIONS:
 * - @Service → Marks this as a service component (Spring manages it)
 * - @RequiredArgsConstructor → Lombok generates a constructor for all 'final' fields
 *                              This is the recommended way to do Dependency Injection!
 * - @Slf4j → Lombok creates a logger: log.info(), log.error(), log.debug()
 * - @Transactional → Wraps methods in a database transaction
 *                     If anything fails, ALL changes are rolled back
 *
 * FLOW:
 * Controller → Service (this class) → Repository → Database
 *
 * The service converts between DTOs and Entities:
 *   DTO → Entity (when saving to database)
 *   Entity → DTO (when sending to controller/view)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentServiceImpl implements StudentService {

    // Spring automatically injects this (constructor injection via @RequiredArgsConstructor)
    private final StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true) // Read-only transaction (better performance for queries)
    public Page<StudentDto> getAllStudents(int pageNo, int pageSize,
                                           String sortField, String sortDirection) {
        log.debug("Fetching students - page: {}, size: {}, sort: {} {}", 
                  pageNo, pageSize, sortField, sortDirection);

        // Create Sort object based on direction
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        // Create Pageable object (page number is 0-based)
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Fetch from database
        Page<Student> studentPage = studentRepository.findAll(pageable);

        // Convert Entity page to DTO page
        return studentPage.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long id) {
        log.debug("Fetching student with id: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found with id: {}", id);
                    return new ResourceNotFoundException("Student not found with id: " + id);
                });

        return convertToDto(student);
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        log.info("Creating new student: {} {}", studentDto.getFirstName(), studentDto.getLastName());

        // Convert DTO to Entity
        Student student = convertToEntity(studentDto);

        // Save to database (JPA auto-generates the ID)
        Student savedStudent = studentRepository.save(student);

        log.info("Student created successfully with id: {}", savedStudent.getId());
        return convertToDto(savedStudent);
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        log.info("Updating student with id: {}", id);

        // First, check if the student exists
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Update fields
        existingStudent.setFirstName(studentDto.getFirstName());
        existingStudent.setLastName(studentDto.getLastName());
        existingStudent.setEmail(studentDto.getEmail());
        existingStudent.setDepartment(studentDto.getDepartment());
        existingStudent.setPhone(studentDto.getPhone());
        existingStudent.setAddress(studentDto.getAddress());

        // Save updated entity
        Student updatedStudent = studentRepository.save(existingStudent);

        log.info("Student updated successfully: {}", id);
        return convertToDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);

        // Check if student exists before deleting
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
        log.info("Student deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDto> searchStudents(String keyword, int pageNo, int pageSize,
                                            String sortField, String sortDirection) {
        log.debug("Searching students with keyword: '{}' - page: {}", keyword, pageNo);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Use custom search query
        Page<Student> studentPage = studentRepository.searchStudents(keyword, pageable);

        return studentPage.map(this::convertToDto);
    }

    // ==================== HELPER METHODS ====================

    /**
     * Convert Entity → DTO
     * Used when sending data FROM database TO the view/controller
     */
    private StudentDto convertToDto(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .department(student.getDepartment())
                .phone(student.getPhone())
                .address(student.getAddress())
                .build();
    }

    /**
     * Convert DTO → Entity
     * Used when saving data FROM the form TO the database
     */
    private Student convertToEntity(StudentDto dto) {
        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }
}
