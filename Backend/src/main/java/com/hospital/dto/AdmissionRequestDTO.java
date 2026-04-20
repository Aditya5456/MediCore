package com.hospital.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class AdmissionRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotNull(message = "Admit date is required")
    private LocalDate admitDate;

    // Optional on admission — set when discharging
    private LocalDate dischargeDate;

    // ── Constructors ─────────────────────────────────────────────────────────
    public AdmissionRequestDTO() {}

    public AdmissionRequestDTO(Integer patientId, Integer doctorId, LocalDate admitDate, LocalDate dischargeDate) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.admitDate = admitDate;
        this.dischargeDate = dischargeDate;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public LocalDate getAdmitDate() { return admitDate; }
    public void setAdmitDate(LocalDate admitDate) { this.admitDate = admitDate; }

    public LocalDate getDischargeDate() { return dischargeDate; }
    public void setDischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer patientId;
        private Integer doctorId;
        private LocalDate admitDate;
        private LocalDate dischargeDate;

        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder doctorId(Integer doctorId) { this.doctorId = doctorId; return this; }
        public Builder admitDate(LocalDate admitDate) { this.admitDate = admitDate; return this; }
        public Builder dischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; return this; }

        public AdmissionRequestDTO build() {
            return new AdmissionRequestDTO(patientId, doctorId, admitDate, dischargeDate);
        }
    }

    @Override
    public String toString() {
        return "AdmissionRequestDTO{" +
                "patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", admitDate=" + admitDate +
                ", dischargeDate=" + dischargeDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdmissionRequestDTO that = (AdmissionRequestDTO) o;
        return patientId != null && patientId.equals(that.patientId) &&
               doctorId != null && doctorId.equals(that.doctorId) &&
               admitDate != null && admitDate.equals(that.admitDate);
    }

    @Override
    public int hashCode() {
        int result = patientId != null ? patientId.hashCode() : 0;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (admitDate != null ? admitDate.hashCode() : 0);
        return result;
    }
}
