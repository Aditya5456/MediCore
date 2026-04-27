package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Diagnosis and treatment notes recorded during an admission.
 * One Admission → One MedicalRecord (One-to-One in business terms,
 * but modelled as Many-to-One here to allow multiple notes per admission
 * if needed in future — consistent with the SQL schema).
 */
@Entity
@Table(name = "MEDICAL_RECORD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @NotNull(message = "Admission is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotBlank(message = "Diagnosis is required")
    @Column(name = "diagnosis", nullable = false, length = 200)
    private String diagnosis;

    @Column(name = "treatment", length = 200)
    private String treatment;

    @NotNull(message = "Record date is required")
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;
}
