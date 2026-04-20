package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Represents a scheduled meeting between a Patient and a Doctor.
 * Status lifecycle: Scheduled → Completed | Cancelled
 */
@Entity
@Table(name = "APPOINTMENT")
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
    private String status = "Scheduled";

    @Size(max = 200)
    @Column(name = "notes", length = 200)
    private String notes;

    // ── Constructors ─────────────────────────────────────────────────────────
    public Appointment() {}

    public Appointment(Patient patient, Doctor doctor, LocalDate appointmentDate, String appointmentTime,
                       String status, String notes) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.notes = notes;
    }

    public Appointment(Integer appointmentId, Patient patient, Doctor doctor, LocalDate appointmentDate,
                       String appointmentTime, String status, String notes) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.notes = notes;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer appointmentId;
        private Patient patient;
        private Doctor doctor;
        private LocalDate appointmentDate;
        private String appointmentTime;
        private String status = "Scheduled";
        private String notes;

        public Builder appointmentId(Integer appointmentId) { this.appointmentId = appointmentId; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }
        public Builder doctor(Doctor doctor) { this.doctor = doctor; return this; }
        public Builder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public Builder appointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }

        public Appointment build() {
            Appointment appointment = new Appointment(patient, doctor, appointmentDate, appointmentTime, status, notes);
            appointment.appointmentId = this.appointmentId;
            return appointment;
        }
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return appointmentId != null && appointmentId.equals(that.appointmentId);
    }

    @Override
    public int hashCode() {
        return appointmentId != null ? appointmentId.hashCode() : 0;
    }
}
