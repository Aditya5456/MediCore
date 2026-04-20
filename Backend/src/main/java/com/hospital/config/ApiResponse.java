package com.hospital.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Wraps every successful API response in a consistent envelope:
 *
 * {
 *   "success": true,
 *   "message": "Patient registered successfully",
 *   "data":    { ... }
 * }
 *
 * This makes life easier on the frontend — it always checks `success`
 * before reading `data`, and can show `message` in a toast notification.
 *
 * @param <T>  the type of the data payload (PatientResponseDTO, List<...>, etc.)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String  message;
    private T       data;

    // ── Factory helpers keep controller code clean ────────────────────────────

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ok(null, data);
    }

    public static ApiResponse<Void> ok(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .build();
    }
}
