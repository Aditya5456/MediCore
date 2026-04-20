package com.hospital.dto;

import java.time.LocalDate;

public class MedicalRecordResponseDTO {
    private Integer   recordId;
    private Integer   admissionId;
    private Integer   patientId;
    private String    patientName;
    private Integer   doctorId;
    private String    doctorName;
    private String    doctorSpecialization;
    private String    diagnosis;
    private String    treatment;
    private LocalDate recordDate;

    // ── Constructors ─────────────────────────────────────────────────────────
    public MedicalRecordResponseDTO() {}

    public MedicalRecordResponseDTO(Integer recordId, Integer admissionId, Integer patientId, String patientName,
                                     Integer doctorId, String doctorName, String doctorSpecialization,
                                     String diagnosis, String treatment, LocalDate recordDate) {
        this.recordId = recordId;
        this.admissionId = admissionId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSpecialization = doctorSpecialization;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.recordDate = recordDate;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public Integer getAdmissionId() { return admissionId; }
    public void setAdmissionId(Integer admissionId) { this.admissionId = admissionId; }

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
        private Integer recordId;
        private Integer admissionId;
        private Integer patientId;
        private String patientName;
        private Integer doctorId;
        private String doctorName;
        private String doctorSpecialization;
        private String diagnosis;
        private String treatment;
        private LocalDate recordDate;

        public Builder recordId(Integer recordId) { this.recordId = recordId; return this; }
        public Builder admissionId(Integer admissionId) { this.admissionId = admissionId; return this; }
        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder doctorId(Integer doctorId) { this.doctorId = doctorId; return this; }
        public Builder doctorName(String doctorName) { this.doctorName = doctorName; return this; }
        public Builder doctorSpecialization(String doctorSpecialization) { this.doctorSpecialization = doctorSpecialization; return this; }
        public Builder diagnosis(String diagnosis) { this.diagnosis = diagnosis; return this; }
        public Builder treatment(String treatment) { this.treatment = treatment; return this; }
        public Builder recordDate(LocalDate recordDate) { this.recordDate = recordDate; return this; }

        public MedicalRecordResponseDTO build() {
            return new MedicalRecordResponseDTO(recordId, admissionId, patientId, patientName, doctorId, doctorName,
                    doctorSpecialization, diagnosis, treatment, recordDate);
        }
    }

    @Override
    public String toString() {
        return "MedicalRecordResponseDTO{" +
                "recordId=" + recordId +
                ", admissionId=" + admissionId +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorSpecialization='" + doctorSpecialization + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatment='" + treatment + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecordResponseDTO that = (MedicalRecordResponseDTO) o;
        return recordId != null && recordId.equals(that.recordId);
    }

    @Override
    public int hashCode() {
        return recordId != null ? recordId.hashCode() : 0;
    }
}
