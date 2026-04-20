package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Records a patient being admitted to the hospital under a doctor's care.
 * One-to-One with MedicalRecord and Billing.
 */
@Entity
@Table(name = "ADMISSION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admission_id")
    private Integer admissionId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull
    @Column(name = "admit_date", nullable = false)
    private LocalDate admitDate;

    // Null means patient is still admitted
    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Column(name = "total_charges")
    @Builder.Default
    private Integer totalCharges = 0;
}
