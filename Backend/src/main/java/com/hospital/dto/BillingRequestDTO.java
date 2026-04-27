package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

// ─── Billing Request DTO ─────────────────────────────────────────────────────

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Admission ID is required")
    private Integer admissionId;

    @NotNull(message = "Bill date is required")
    private LocalDate billDate;

    @PositiveOrZero(message = "Room charges cannot be negative")
    @Builder.Default
    private Integer roomCharges = 0;

    @PositiveOrZero(message = "Medicine charges cannot be negative")
    @Builder.Default
    private Integer medicineCharges = 0;

    @PositiveOrZero(message = "Doctor charges cannot be negative")
    @Builder.Default
    private Integer doctorCharges = 0;

    @PositiveOrZero(message = "Paid amount cannot be negative")
    @Builder.Default
    private Integer paidAmount = 0;
}
