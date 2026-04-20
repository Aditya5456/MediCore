package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a registered patient.
 * A patient can have many appointments, admissions and billing records.
 */
@Entity
@Table(name = "PATIENT")
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
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Billing> billings;

    // ── Constructors ───────────────────────────────────────────────────────────
    public Patient() {}

    public Patient(String name, LocalDate dob, String gender, String bloodGroup, String phone, String email, String address) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public List<Billing> getBillings() { return billings; }
    public void setBillings(List<Billing> billings) { this.billings = billings; }

    // ── Builder pattern ───────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer patientId;
        private String name;
        private LocalDate dob;
        private String gender;
        private String bloodGroup;
        private String phone;
        private String email;
        private String address;
        private List<Appointment> appointments;
        private List<Billing> billings;

        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder dob(LocalDate dob) { this.dob = dob; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder bloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder appointments(List<Appointment> appointments) { this.appointments = appointments; return this; }
        public Builder billings(List<Billing> billings) { this.billings = billings; return this; }

        public Patient build() {
            Patient patient = new Patient(name, dob, gender, bloodGroup, phone, email, address);
            patient.patientId = this.patientId;
            patient.appointments = this.appointments;
            patient.billings = this.billings;
            return patient;
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return patientId != null && patientId.equals(patient.patientId);
    }

    @Override
    public int hashCode() {
        return patientId != null ? patientId.hashCode() : 0;
    }
}
