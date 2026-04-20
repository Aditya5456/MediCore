package com.hospital.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Uniform error body returned for every failed API call.
 *
 * Success response shape  →  { data: {...},  success: true  }
 * Error   response shape  →  { timestamp, status, error, message, path, fieldErrors? }
 *
 * Example output:
 * {
 *   "timestamp": "2025-03-10T09:15:30",
 *   "status":    404,
 *   "error":     "Not Found",
 *   "message":   "Patient not found with id: '9999'",
 *   "path":      "/api/patients/9999"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)   // omit null fields from JSON output
public class ApiError {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private int    status;
    private String error;
    private String message;
    private String path;

    /**
     * Present only for validation errors — maps field name → error message.
     * e.g.  { "phone": "Invalid phone number", "name": "Name is required" }
     */
    private Map<String, String> fieldErrors;
}
