package com.example.ecommerce.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // 404 - Resource Not Found
    // =========================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // =========================
    // 400 - Bad Request
    // =========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // =========================
    // 401 - Unauthorized (JWT)
    // =========================
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiError> handleUnauthorized(
            SecurityException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // =========================
    // 403 - Access Denied
    // =========================
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(
            Exception ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.FORBIDDEN,
                "FORBIDDEN",
                "You are not allowed to access this resource",
                request.getRequestURI()
        );
    }

    // =========================
    // 500 - Internal Server Error
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobal(
            Exception ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // =========================
    // Helper Method
    // =========================
    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String error,
            String message,
            String path) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                path
        );

        return new ResponseEntity<>(apiError, status);
    }

    // =========================
    // Inner Error Response Class
    // (NO EXTRA FILE REQUIRED)
    // =========================
    static class ApiError {
        public LocalDateTime timestamp;
        public int status;
        public String error;
        public String message;
        public String path;

        public ApiError(LocalDateTime timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }
    }

    // =========================
    // Inner Custom Exception
    // =========================
    static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
