package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * Represents a hospital department (e.g. Cardiology, Neurology).
 * One Department → many Doctors.
 */
@Entity
@Table(name = "DEPARTMENT")
@Data                        // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor           // Lombok: default constructor (required by JPA)
@AllArgsConstructor          // Lombok: all-args constructor
@Builder                     // Lombok: builder pattern
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Integer deptId;

    @NotBlank(message = "Department name is required")
    @Size(max = 50)
    @Column(name = "dept_name", nullable = false, unique = true, length = 50)
    private String deptName;

    @NotBlank(message = "Location is required")
    @Size(max = 50)
    @Column(name = "location", nullable = false, length = 50)
    private String location;

    /**
     * Bidirectional mapping — one department has many doctors.
     * mappedBy = "department"  →  the 'department' field in Doctor owns the FK.
     * FetchType.LAZY           →  doctors are NOT loaded unless explicitly accessed
     *                             (prevents N+1 performance problems).
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude   // Lombok: prevent infinite recursion in toString()
    @EqualsAndHashCode.Exclude
    private List<Doctor> doctors;
}
