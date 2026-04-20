package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

// ─── Appointment Request DTO ────────────────────────────────────────────────

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in the past")
    private LocalDate appointmentDate;

    @Size(max = 10)
    private String appointmentTime;

    @Size(max = 200)
    private String notes;
}
