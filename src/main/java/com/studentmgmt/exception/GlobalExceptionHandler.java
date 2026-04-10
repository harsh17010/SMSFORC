package com.studentmgmt.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * GLOBAL EXCEPTION HANDLER
 * ==========================
 * @ControllerAdvice is a special Spring annotation that catches exceptions
 * thrown by ANY controller in the application.
 *
 * Think of it as a "safety net" — if any controller throws an exception,
 * this class catches it and shows a user-friendly error page instead of
 * an ugly stack trace.
 *
 * HOW IT WORKS:
 * 1. Controller throws an exception
 * 2. Spring looks for a matching @ExceptionHandler in this class
 * 3. The handler method runs and returns an error page
 *
 * WITHOUT this: User sees a raw error page or stack trace
 * WITH this:    User sees a clean, helpful error page
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle "Resource Not Found" exceptions.
     * Returns our custom 404 error page.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        log.error("Resource not found: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404"; // Returns templates/error/404.html
    }

    /**
     * Handle "No Handler Found" exceptions (invalid URLs).
     * Returns our custom 404 error page.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, Model model) {
        log.error("Page not found: {}", ex.getRequestURL());
        model.addAttribute("errorMessage", "The page you're looking for doesn't exist.");
        return "error/404";
    }

    /**
     * Handle IllegalArgumentException (bad input).
     * Returns our custom 400 error page.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        log.error("Bad request: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/400";
    }

    /**
     * Handle AccessDeniedException (forbidden).
     * Returns our custom 403 error page.
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(
            org.springframework.security.access.AccessDeniedException ex, Model model) {
        log.error("Access denied: {}", ex.getMessage());
        model.addAttribute("errorMessage", "You don't have permission to access this resource.");
        return "error/403";
    }

    /**
     * Handle ALL other exceptions (catch-all).
     * This is the last resort — catches anything we didn't specifically handle.
     * Returns our custom 500 error page.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGlobalException(Exception ex, Model model) {
        log.error("Unexpected error occurred: ", ex);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        return "error/500";
    }
}
