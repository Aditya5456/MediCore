package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a registered patient.
 * A patient can have many appointments, admissions and billing records.
 */
@Entity
@Table(name = "PATIENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patientId;

    @NotBlank(message = "Patient name is required")
    @Size(max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    /**
     * CHECK constraint is enforced at DB level (hospital.sql).
     * At application level we use @Pattern to validate.
     */
    @Pattern(regexp = "Male|Female|Other",
             message = "Gender must be Male, Female, or Other")
    @Column(name = "gender", length = 10)
    private String gender;

    @Size(max = 5)
    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit Indian phone number")
    @Column(name = "phone", nullable = false, unique = true, length = 15)
    private String phone;

    @Email(message = "Enter a valid email address")
    @Column(name = "email", length = 50)
    private String email;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    // ── Relationships ───────────────────────────────────────────────────────

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Billing> billings;
}
