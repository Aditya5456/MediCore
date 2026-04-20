package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * Represents a doctor who belongs to one Department.
 * A doctor can handle many Appointments.
 */
@Entity
@Table(name = "DOCTOR")
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
    private List<Appointment> appointments;

    // ── Constructors ─────────────────────────────────────────────────────────
    public Doctor() {}

    public Doctor(String name, String specialization, String phone, String email, Integer salary) {
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
    }

    public Doctor(String name, String specialization, String phone, String email, Integer salary, Department department) {
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.department = department;
    }

    public Doctor(Integer doctorId, String name, String specialization, String phone, String email, Integer salary,
                  Department department, List<Appointment> appointments) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.department = department;
        this.appointments = appointments;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getSalary() { return salary; }
    public void setSalary(Integer salary) { this.salary = salary; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer doctorId;
        private String name;
        private String specialization;
        private String phone;
        private String email;
        private Integer salary;
        private Department department;
        private List<Appointment> appointments;

        public Builder doctorId(Integer doctorId) { this.doctorId = doctorId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder specialization(String specialization) { this.specialization = specialization; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder salary(Integer salary) { this.salary = salary; return this; }
        public Builder department(Department department) { this.department = department; return this; }
        public Builder appointments(List<Appointment> appointments) { this.appointments = appointments; return this; }

        public Doctor build() {
            Doctor doctor = new Doctor(name, specialization, phone, email, salary, department);
            doctor.doctorId = this.doctorId;
            doctor.appointments = this.appointments;
            return doctor;
        }
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return doctorId != null && doctorId.equals(doctor.doctorId);
    }

    @Override
    public int hashCode() {
        return doctorId != null ? doctorId.hashCode() : 0;
    }
}
