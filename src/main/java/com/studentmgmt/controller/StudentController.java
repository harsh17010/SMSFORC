package com.studentmgmt.controller;

import com.studentmgmt.dto.StudentDto;
import com.studentmgmt.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * STUDENT CONTROLLER
 * ====================
 * Handles all student-related web pages (CRUD operations).
 *
 * URL MAPPINGS:
 *   GET  /students            → List all students (with pagination & sorting)
 *   GET  /students/add        → Show "Add Student" form
 *   POST /students/save       → Save a new student
 *   GET  /students/edit/{id}  → Show "Edit Student" form
 *   POST /students/update/{id} → Update an existing student
 *   GET  /students/delete/{id} → Delete a student (ADMIN only)
 *   GET  /students/search     → Search students by keyword
 *
 * ALL endpoints in this controller require authentication (login).
 * The /students/delete/** endpoint requires ADMIN role (configured in SecurityConfig).
 */
@Controller
@RequestMapping("/students")  // Base URL prefix for all methods in this controller
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    // Default pagination settings
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final String DEFAULT_SORT_FIELD = "firstName";
    private static final String DEFAULT_SORT_DIR = "asc";

    /**
     * LIST ALL STUDENTS (with pagination, sorting, and search)
     * ==========================================================
     * This is the main student list page.
     *
     * Query Parameters:
     * - page:    Page number (default: 0, which is the first page)
     * - size:    Number of students per page (default: 5)
     * - sort:    Field to sort by (default: firstName)
     * - dir:     Sort direction: "asc" or "desc" (default: asc)
     * - keyword: Search keyword (optional)
     *
     * Example URLs:
     *   /students                       → First page, default sort
     *   /students?page=2&size=10       → Third page, 10 per page
     *   /students?sort=email&dir=desc  → Sort by email, descending
     *   /students?keyword=John          → Search for "John"
     */
    @GetMapping
    public String listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "firstName") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(required = false) String keyword,
            Model model) {

        log.debug("Listing students - page: {}, size: {}, sort: {} {}, keyword: {}", 
                  page, size, sort, dir, keyword);

        Page<StudentDto> studentPage;

        // If a search keyword is provided, search; otherwise, list all
        if (keyword != null && !keyword.trim().isEmpty()) {
            studentPage = studentService.searchStudents(keyword.trim(), page, size, sort, dir);
            model.addAttribute("keyword", keyword);
        } else {
            studentPage = studentService.getAllStudents(page, size, sort, dir);
        }

        // Add data to the model for Thymeleaf to render
        model.addAttribute("students", studentPage.getContent());      // List of students on this page
        model.addAttribute("currentPage", page);                        // Current page number
        model.addAttribute("totalPages", studentPage.getTotalPages()); // Total number of pages
        model.addAttribute("totalItems", studentPage.getTotalElements()); // Total number of students
        model.addAttribute("pageSize", size);                           // Items per page
        model.addAttribute("sortField", sort);                          // Current sort field
        model.addAttribute("sortDir", dir);                             // Current sort direction
        model.addAttribute("reverseSortDir", dir.equals("asc") ? "desc" : "asc"); // Toggle direction

        return "students/list"; // Returns templates/students/list.html
    }

    /**
     * SHOW ADD STUDENT FORM
     * ======================
     * Displays an empty form for adding a new student.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        log.debug("Showing add student form");
        model.addAttribute("student", new StudentDto());
        return "students/add"; // Returns templates/students/add.html
    }

    /**
     * SAVE NEW STUDENT
     * ==================
     * Processes the submitted add student form.
     *
     * @param studentDto  the form data (auto-bound by Spring from form fields)
     * @param result      contains validation errors (if any)
     * @param redirectAttributes for flash messages after redirect
     */
    @PostMapping("/save")
    public String saveStudent(
            @Valid @ModelAttribute("student") StudentDto studentDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        log.info("Saving new student: {} {}", studentDto.getFirstName(), studentDto.getLastName());

        // If validation errors exist, show the form again with error messages
        if (result.hasErrors()) {
            log.warn("Validation failed: {} errors", result.getErrorCount());
            return "students/add";
        }

        // Save the student
        studentService.createStudent(studentDto);

        // Flash success message
        redirectAttributes.addFlashAttribute("successMessage", "Student added successfully!");

        return "redirect:/students";
    }

    /**
     * SHOW EDIT STUDENT FORM
     * ========================
     * Displays a pre-filled form for editing an existing student.
     *
     * @param id the student ID from the URL path
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.debug("Showing edit form for student id: {}", id);

        StudentDto student = studentService.getStudentById(id);
        model.addAttribute("student", student);

        return "students/edit"; // Returns templates/students/edit.html
    }

    /**
     * UPDATE EXISTING STUDENT
     * =========================
     * Processes the submitted edit student form.
     */
    @PostMapping("/update/{id}")
    public String updateStudent(
            @PathVariable Long id,
            @Valid @ModelAttribute("student") StudentDto studentDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        log.info("Updating student id: {}", id);

        if (result.hasErrors()) {
            log.warn("Validation failed: {} errors", result.getErrorCount());
            studentDto.setId(id);
            return "students/edit";
        }

        studentService.updateStudent(id, studentDto);
        redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");

        return "redirect:/students";
    }

    /**
     * DELETE STUDENT
     * ================
     * Deletes a student by ID.
     * IMPORTANT: Only users with ADMIN role can access this endpoint!
     * (Configured in SecurityConfig.java)
     */
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Deleting student id: {}", id);

        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");

        return "redirect:/students";
    }
}
