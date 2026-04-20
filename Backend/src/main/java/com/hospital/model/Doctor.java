package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * Represents a doctor who belongs to one Department.
 * A doctor can handle many Appointments.
 */
@Entity
@Table(name = "DOCTOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer doctorId;

    @NotBlank(message = "Doctor name is required")
    @Size(max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Specialization is required")
    @Size(max = 50)
    @Column(name = "specialization", nullable = false, length = 50)
    private String specialization;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit phone number")
    @Column(name = "phone", unique = true, length = 15)
    private String phone;

    @Email(message = "Enter a valid email address")
    @Column(name = "email", unique = true, length = 50)
    private String email;

    /**
     * Business rule (mirrors DB trigger): salary must be >= 30,000.
     */
    @NotNull(message = "Salary is required")
    @Min(value = 30000, message = "Salary must be at least ₹30,000")
    @Column(name = "salary", nullable = false)
    private Integer salary;

    // ── Relationships ───────────────────────────────────────────────────────

    /**
     * Many doctors belong to one department.
     * The FK column 'dept_id' lives in the DOCTOR table.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_id")
    private Department department;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Appointment> appointments;
}
