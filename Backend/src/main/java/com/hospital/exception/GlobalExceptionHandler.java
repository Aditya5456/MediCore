package com.hospital.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  GlobalExceptionHandler
 *
 *  @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 *  Intercepts exceptions thrown anywhere in the application and converts
 *  them into clean, consistent JSON error responses.
 *
 *  Handler priority (Spring tries them top to bottom):
 *   1. handleMethodArgumentNotValid  → @Valid failures (field-level errors)
 *   2. handleResourceNotFound        → 404 when entity doesn't exist
 *   3. handleDuplicateResource       → 409 on unique constraint violation
 *   4. handleBadRequest              → 400 for business rule violations
 *   5. handleAllUncaught             → 500 safety net for unexpected errors
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // ── 1. Validation errors (@Valid / @Validated) ────────────────────────────
    /**
     * Triggered when a @RequestBody fails Bean Validation.
     * Collects ALL field errors into a map so the client knows
     * exactly which fields are wrong in a single response.
     *
     * Example response:
     * {
     *   "status": 400,
     *   "error":  "Validation Failed",
     *   "fieldErrors": {
     *       "phone": "Invalid phone number",
     *       "name":  "Name is required"
     *   }
     * }
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult()
          .getAllErrors()
          .forEach(error -> {
              String fieldName = ((FieldError) error).getField();
              String message   = error.getDefaultMessage();
              fieldErrors.put(fieldName, message);
          });

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("One or more fields failed validation. See 'fieldErrors' for details.")
                .fieldErrors(fieldErrors)
                .build();

        log.warn("Validation failed: {}", fieldErrors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // ── 2. Resource not found (404) ───────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        log.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // ── 3. Duplicate resource (409) ───────────────────────────────────────────
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        log.warn("Duplicate resource: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    // ── 4. Business rule violation (400) ─────────────────────────────────────
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        log.warn("Bad request: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // ── 5. Safety net — unexpected errors (500) ───────────────────────────────
    /**
     * Catches any exception not handled above.
     * Logs the full stack trace (important!) but returns only a generic
     * message to the client — never expose internal stack traces via API.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(
            Exception ex,
            HttpServletRequest request) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred. Please contact support.")
                .path(request.getRequestURI())
                .build();

        log.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
