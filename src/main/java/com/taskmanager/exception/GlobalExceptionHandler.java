package com.taskmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class intercepts exceptions thrown by controllers and converts them into
 * standardized HTTP responses with appropriate status codes and error details.
 * <p>
 * Supported exception types include:
 * <ul>
 *     <li>{@link IllegalArgumentException} - returns HTTP 400 Bad Request</li>
 *     <li>{@link EntityNotFoundException} - returns HTTP 404 Not Found</li>
 *     <li>{@link Exception} - returns HTTP 500 Internal Server Error for any other exceptions</li>
 * </ul>
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link IllegalArgumentException} thrown by controllers.
     *
     * @param ex the exception instance
     * @return a {@link ResponseEntity} containing error details and HTTP 400 status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequest(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles {@link EntityNotFoundException} thrown by controllers.
     *
     * @param ex the exception instance
     * @return a {@link ResponseEntity} containing error details and HTTP 404 status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles all other exceptions that are not specifically caught.
     *
     * @param ex the exception instance
     * @return a {@link ResponseEntity} containing error details and HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Builds a standardized error response body for the given HTTP status and message.
     *
     * @param status the HTTP status to return
     * @param message the error message to include in the response
     * @return a {@link ResponseEntity} containing the structured error response
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, status);
    }
}
