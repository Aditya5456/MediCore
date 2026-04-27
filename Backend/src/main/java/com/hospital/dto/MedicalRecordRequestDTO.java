package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicalRecordRequestDTO {

    @NotNull(message = "Admission ID is required")
    private Integer admissionId;

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 200)
    private String diagnosis;

    @Size(max = 200)
    private String treatment;

    @NotNull(message = "Record date is required")
    private LocalDate recordDate;
}
