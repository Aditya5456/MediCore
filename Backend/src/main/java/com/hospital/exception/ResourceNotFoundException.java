package com.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ── 404 Not Found ────────────────────────────────────────────────────────────
/**
 * Thrown when a requested resource (patient, doctor, etc.) does not exist.
 * @ResponseStatus maps this to HTTP 404 when thrown from a controller.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName    = fieldName;
        this.fieldValue   = fieldValue;
    }

    // Convenience overload — e.g. throw new ResourceNotFoundException("Patient", 1001)
    public ResourceNotFoundException(String resourceName, Object id) {
        this(resourceName, "id", id);
    }
}
