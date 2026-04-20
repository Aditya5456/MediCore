package com.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ── 400 Bad Request ──────────────────────────────────────────────────────────
/**
 * Thrown when a request violates a business rule that cannot be caught
 * by Bean Validation alone.
 * Examples:
 *   – paid amount exceeds total bill amount
 *   – trying to book an appointment for a discharged patient
 *   – appointment status transition is invalid (e.g. Cancelled → Completed)
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
