package com.studentmgmt.controller;

import com.studentmgmt.repository.StudentRepository;
import com.studentmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * MAIN CONTROLLER
 * =================
 * Handles the home page and dashboard.
 *
 * URL MAPPINGS:
 *   GET /          → Home page (redirects to login or dashboard)
 *   GET /dashboard → Dashboard with statistics
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    /**
     * HOME PAGE
     * ==========
     * Redirects to login page. If user is already logged in,
     * Spring Security will redirect them to the dashboard.
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    /**
     * DASHBOARD PAGE
     * ================
     * Shows an overview with statistics.
     * This page is protected — only logged-in users can see it.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.debug("Loading dashboard");

        // Add statistics to the model
        long totalStudents = studentRepository.count();
        long totalUsers = userRepository.count();

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalUsers", totalUsers);

        return "dashboard"; // Returns templates/dashboard.html
    }
}
