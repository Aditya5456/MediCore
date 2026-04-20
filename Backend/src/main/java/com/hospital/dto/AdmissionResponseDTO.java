package com.hospital.dto;

import java.time.LocalDate;

public class AdmissionResponseDTO {
    private Integer   admissionId;
    private Integer   patientId;
    private String    patientName;
    private Integer   doctorId;
    private String    doctorName;
    private String    doctorSpecialization;
    private LocalDate admitDate;
    private LocalDate dischargeDate;
    private Integer   totalCharges;
    private String    status;      // "Admitted" or "Discharged" — derived field

    // ── Constructors ─────────────────────────────────────────────────────────
    public AdmissionResponseDTO() {}

    public AdmissionResponseDTO(Integer admissionId, Integer patientId, String patientName, Integer doctorId,
                                 String doctorName, String doctorSpecialization, LocalDate admitDate,
                                 LocalDate dischargeDate, Integer totalCharges, String status) {
        this.admissionId = admissionId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSpecialization = doctorSpecialization;
        this.admitDate = admitDate;
        this.dischargeDate = dischargeDate;
        this.totalCharges = totalCharges;
        this.status = status;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
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

    public LocalDate getAdmitDate() { return admitDate; }
    public void setAdmitDate(LocalDate admitDate) { this.admitDate = admitDate; }

    public LocalDate getDischargeDate() { return dischargeDate; }
    public void setDischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; }

    public Integer getTotalCharges() { return totalCharges; }
    public void setTotalCharges(Integer totalCharges) { this.totalCharges = totalCharges; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer admissionId;
        private Integer patientId;
        private String patientName;
        private Integer doctorId;
        private String doctorName;
        private String doctorSpecialization;
        private LocalDate admitDate;
        private LocalDate dischargeDate;
        private Integer totalCharges;
        private String status;

        public Builder admissionId(Integer admissionId) { this.admissionId = admissionId; return this; }
        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder doctorId(Integer doctorId) { this.doctorId = doctorId; return this; }
        public Builder doctorName(String doctorName) { this.doctorName = doctorName; return this; }
        public Builder doctorSpecialization(String doctorSpecialization) { this.doctorSpecialization = doctorSpecialization; return this; }
        public Builder admitDate(LocalDate admitDate) { this.admitDate = admitDate; return this; }
        public Builder dischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; return this; }
        public Builder totalCharges(Integer totalCharges) { this.totalCharges = totalCharges; return this; }
        public Builder status(String status) { this.status = status; return this; }

        public AdmissionResponseDTO build() {
            return new AdmissionResponseDTO(admissionId, patientId, patientName, doctorId, doctorName,
                    doctorSpecialization, admitDate, dischargeDate, totalCharges, status);
        }
    }

    @Override
    public String toString() {
        return "AdmissionResponseDTO{" +
                "admissionId=" + admissionId +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorSpecialization='" + doctorSpecialization + '\'' +
                ", admitDate=" + admitDate +
                ", dischargeDate=" + dischargeDate +
                ", totalCharges=" + totalCharges +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdmissionResponseDTO that = (AdmissionResponseDTO) o;
        return admissionId != null && admissionId.equals(that.admissionId);
    }

    @Override
    public int hashCode() {
        return admissionId != null ? admissionId.hashCode() : 0;
    }
}
