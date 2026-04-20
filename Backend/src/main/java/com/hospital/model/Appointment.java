package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Represents a scheduled meeting between a Patient and a Doctor.
 * Status lifecycle: Scheduled → Completed | Cancelled
 */
@Entity
@Table(name = "APPOINTMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;

    /**
     * Many appointments belong to one patient.
     * FetchType.EAGER so patient details are always loaded with the appointment.
     */
    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in the past")
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Size(max = 10)
    @Column(name = "appointment_time", length = 10)
    private String appointmentTime;

    /**
     * Allowed values: Scheduled, Completed, Cancelled
     * Default set at DB level (DEFAULT 'Scheduled') and at service level.
     */
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "Scheduled";

    @Size(max = 200)
    @Column(name = "notes", length = 200)
    private String notes;
}
