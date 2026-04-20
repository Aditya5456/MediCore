package com.hospital.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class MedicalRecordRequestDTO {

    @NotNull(message = "Admission ID is required")
    private Integer admissionId;

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 200)
    private String diagnosis;

    @Size(max = 200)
    private String treatment;

    @NotNull(message = "Record date is required")
    private LocalDate recordDate;

    // ── Constructors ─────────────────────────────────────────────────────────
    public MedicalRecordRequestDTO() {}

    public MedicalRecordRequestDTO(Integer admissionId, Integer patientId, Integer doctorId, String diagnosis,
                                    String treatment, LocalDate recordDate) {
        this.admissionId = admissionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.recordDate = recordDate;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getAdmissionId() { return admissionId; }
    public void setAdmissionId(Integer admissionId) { this.admissionId = admissionId; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer admissionId;
        private Integer patientId;
        private Integer doctorId;
        private String diagnosis;
        private String treatment;
        private LocalDate recordDate;

        public Builder admissionId(Integer admissionId) { this.admissionId = admissionId; return this; }
        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder doctorId(Integer doctorId) { this.doctorId = doctorId; return this; }
        public Builder diagnosis(String diagnosis) { this.diagnosis = diagnosis; return this; }
        public Builder treatment(String treatment) { this.treatment = treatment; return this; }
        public Builder recordDate(LocalDate recordDate) { this.recordDate = recordDate; return this; }

        public MedicalRecordRequestDTO build() {
            return new MedicalRecordRequestDTO(admissionId, patientId, doctorId, diagnosis, treatment, recordDate);
        }
    }

    @Override
    public String toString() {
        return "MedicalRecordRequestDTO{" +
                "admissionId=" + admissionId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatment='" + treatment + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecordRequestDTO that = (MedicalRecordRequestDTO) o;
        return admissionId != null && admissionId.equals(that.admissionId) &&
               patientId != null && patientId.equals(that.patientId) &&
               doctorId != null && doctorId.equals(that.doctorId);
    }

    @Override
    public int hashCode() {
        int result = admissionId != null ? admissionId.hashCode() : 0;
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        return result;
    }
}
