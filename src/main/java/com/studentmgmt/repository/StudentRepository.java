package com.studentmgmt.repository;

import com.studentmgmt.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * STUDENT REPOSITORY
 * ===================
 * This interface extends JpaRepository which gives us ALL CRUD operations for FREE:
 *   - save(entity)      → INSERT or UPDATE
 *   - findById(id)      → SELECT by primary key
 *   - findAll()         → SELECT all rows
 *   - deleteById(id)    → DELETE by primary key
 *   - count()           → COUNT rows
 *   - existsById(id)    → Check if row exists
 *
 * HOW SPRING DATA JPA WORKS:
 * ===========================
 * You only need to DECLARE the method — Spring generates the SQL for you!
 * For example, findByEmail() automatically becomes:
 *   SELECT * FROM students WHERE email = ?
 *
 * Method naming convention:
 *   findBy + FieldName + Condition
 *   Examples: findByFirstName, findByEmailContaining, findByDepartmentOrderByLastName
 *
 * @Repository marks this as a data access component.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Search students by first name or last name (case-insensitive).
     * Spring Data JPA translates this method name into an SQL query automatically!
     *
     * The generated SQL is roughly:
     *   SELECT * FROM students
     *   WHERE LOWER(first_name) LIKE LOWER('%keyword%')
     *      OR LOWER(last_name) LIKE LOWER('%keyword%')
     *
     * @param firstName search keyword for first name
     * @param lastName  search keyword for last name
     * @param pageable  pagination and sorting information
     * @return a Page of matching students
     */
    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);

    /**
     * Custom JPQL query for more complex search.
     * Searches across first name, last name, email, and department.
     *
     * @param keyword the search term
     * @param pageable pagination info
     * @return a Page of matching students
     */
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.department) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Student> searchStudents(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Check if a student with the given email already exists.
     * Useful for preventing duplicate registrations.
     */
    boolean existsByEmail(String email);
}
