package com.hospital.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

// ─── Appointment Request DTO ────────────────────────────────────────────────

public class AppointmentRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in the past")
    private LocalDate appointmentDate;

    @Size(max = 10)
    private String appointmentTime;

    @Size(max = 200)
    private String notes;

    // ── Constructors ─────────────────────────────────────────────────────────
    public AppointmentRequestDTO() {}

    public AppointmentRequestDTO(Integer patientId, Integer doctorId, LocalDate appointmentDate, String appointmentTime, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.notes = notes;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer patientId;
        private Integer doctorId;
        private LocalDate appointmentDate;
        private String appointmentTime;
        private String notes;

        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder doctorId(Integer doctorId) { this.doctorId = doctorId; return this; }
        public Builder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public Builder appointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }

        public AppointmentRequestDTO build() {
            return new AppointmentRequestDTO(patientId, doctorId, appointmentDate, appointmentTime, notes);
        }
    }

    @Override
    public String toString() {
        return "AppointmentRequestDTO{" +
                "patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentRequestDTO that = (AppointmentRequestDTO) o;
        return patientId != null && patientId.equals(that.patientId) &&
               doctorId != null && doctorId.equals(that.doctorId) &&
               appointmentDate != null && appointmentDate.equals(that.appointmentDate);
    }

    @Override
    public int hashCode() {
        int result = patientId != null ? patientId.hashCode() : 0;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (appointmentDate != null ? appointmentDate.hashCode() : 0);
        return result;
    }
}
