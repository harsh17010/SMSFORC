package com.studentmgmt.controller;

import com.studentmgmt.dto.UserRegistrationDto;
import com.studentmgmt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * AUTHENTICATION CONTROLLER
 * ===========================
 * Handles login and registration pages.
 *
 * CONTROLLER ROLE IN SPRING MVC:
 * ===============================
 * 1. Receive HTTP request from the browser
 * 2. Call the appropriate service method
 * 3. Add data to the Model (so Thymeleaf can display it)
 * 4. Return the name of the HTML template to render
 *
 * URL MAPPINGS:
 *   GET  /login     → Show login page
 *   GET  /register  → Show registration form
 *   POST /register  → Process registration form
 *
 * @Controller = This class handles web page requests (returns HTML views)
 * (vs @RestController which returns JSON data)
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    /**
     * SHOW LOGIN PAGE
     * ================
     * When user visits /login, show the login form.
     * Spring Security handles the actual authentication logic.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        log.debug("Showing login page");
        return "login"; // Returns templates/login.html
    }

    /**
     * SHOW REGISTRATION FORM
     * =======================
     * When user visits /register, show an empty registration form.
     * We add an empty DTO to the model so Thymeleaf can bind form fields to it.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        log.debug("Showing registration form");
        model.addAttribute("user", new UserRegistrationDto());
        return "register"; // Returns templates/register.html
    }

    /**
     * PROCESS REGISTRATION FORM
     * ==========================
     * When user submits the registration form (POST /register):
     *
     * Steps:
     * 1. @Valid triggers validation on UserRegistrationDto
     * 2. If validation fails → BindingResult has errors → show form again with errors
     * 3. Check if passwords match
     * 4. Check if email already exists
     * 5. If all good → Register user and redirect to login
     *
     * @param registrationDto  the form data (auto-bound by Spring)
     * @param result           contains validation errors (if any)
     * @param redirectAttributes used to pass flash messages between redirects
     */
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") UserRegistrationDto registrationDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        log.info("Processing registration for: {}", registrationDto.getEmail());

        // Check if passwords match
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }

        // Check if email already exists
        if (userService.emailExists(registrationDto.getEmail())) {
            result.rejectValue("email", "error.user", "An account with this email already exists");
        }

        // If there are validation errors, show the form again with error messages
        if (result.hasErrors()) {
            log.warn("Registration validation failed: {} errors", result.getErrorCount());
            return "register";
        }

        // All validations passed — register the user
        userService.registerUser(registrationDto);

        // Flash message (shown after redirect)
        redirectAttributes.addFlashAttribute("successMessage",
                "Registration successful! Please login with your credentials.");

        log.info("User registered successfully: {}", registrationDto.getEmail());
        return "redirect:/login?registered=true";
    }
}
