package com.studentmgmt.exception;

/**
 * RESOURCE NOT FOUND EXCEPTION
 * =============================
 * Custom exception thrown when a requested resource (student, user, etc.)
 * is not found in the database.
 *
 * Extends RuntimeException (unchecked exception) so we don't need
 * to declare it in every method signature.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
