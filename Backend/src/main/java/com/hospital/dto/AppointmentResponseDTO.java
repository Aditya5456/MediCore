package com.hospital.dto;

import java.time.LocalDate;

public class AppointmentResponseDTO {
    private Integer   appointmentId;
    private Integer   patientId;
    private String    patientName;
    private Integer   doctorId;
    private String    doctorName;
    private String    doctorSpecialization;
    private LocalDate appointmentDate;
    private String    appointmentTime;
    private String    status;
    private String    notes;

    // ── Constructors ─────────────────────────────────────────────────────────
    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(Integer appointmentId, Integer patientId, String patientName, Integer doctorId,
                                   String doctorName, String doctorSpecialization, LocalDate appointmentDate,
                                   String appointmentTime, String status, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSpecialization = doctorSpecialization;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.notes = notes;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDoctorSpecialization() { return doctorSpecialization; }
    public void setDoctorSpecialization(String doctorSpecialization) { this.doctorSpecialization = doctorSpecialization; }

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
        private Integer patientId;
        private String patientName;
        private Integer doctorId;
        private String doctorName;
        private String doctorSpecialization;
        private LocalDate appointmentDate;
        private String appointmentTime;
        private String status;
        private String notes;

        public Builder appointmentId(Integer appointmentId) { this.appointmentId = appointmentId; return this; }
        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder doctorId(Integer doctorId) { this.doctorId = doctorId; return this; }
        public Builder doctorName(String doctorName) { this.doctorName = doctorName; return this; }
        public Builder doctorSpecialization(String doctorSpecialization) { this.doctorSpecialization = doctorSpecialization; return this; }
        public Builder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public Builder appointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }

        public AppointmentResponseDTO build() {
            return new AppointmentResponseDTO(appointmentId, patientId, patientName, doctorId, doctorName,
                    doctorSpecialization, appointmentDate, appointmentTime, status, notes);
        }
    }

    @Override
    public String toString() {
        return "AppointmentResponseDTO{" +
                "appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorSpecialization='" + doctorSpecialization + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentResponseDTO that = (AppointmentResponseDTO) o;
        return appointmentId != null && appointmentId.equals(that.appointmentId);
    }

    @Override
    public int hashCode() {
        return appointmentId != null ? appointmentId.hashCode() : 0;
    }
}
