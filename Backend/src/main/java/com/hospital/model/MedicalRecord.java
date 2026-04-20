package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Diagnosis and treatment notes recorded during an admission.
 * One Admission → One MedicalRecord (One-to-One in business terms,
 * but modelled as Many-to-One here to allow multiple notes per admission
 * if needed in future — consistent with the SQL schema).
 */
@Entity
@Table(name = "MEDICAL_RECORD")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @NotNull(message = "Admission is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotBlank(message = "Diagnosis is required")
    @Column(name = "diagnosis", nullable = false, length = 200)
    private String diagnosis;

    @Column(name = "treatment", length = 200)
    private String treatment;

    @NotNull(message = "Record date is required")
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    // ── Constructors ─────────────────────────────────────────────────────────
    public MedicalRecord() {}

    public MedicalRecord(Admission admission, Patient patient, Doctor doctor, String diagnosis, String treatment, LocalDate recordDate) {
        this.admission = admission;
        this.patient = patient;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.recordDate = recordDate;
    }

    public MedicalRecord(Integer recordId, Admission admission, Patient patient, Doctor doctor, String diagnosis,
                         String treatment, LocalDate recordDate) {
        this.recordId = recordId;
        this.admission = admission;
        this.patient = patient;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.recordDate = recordDate;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public Admission getAdmission() { return admission; }
    public void setAdmission(Admission admission) { this.admission = admission; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

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
        private Admission admission;
        private Patient patient;
        private Doctor doctor;
        private String diagnosis;
        private String treatment;
        private LocalDate recordDate;

        public Builder recordId(Integer recordId) { this.recordId = recordId; return this; }
        public Builder admission(Admission admission) { this.admission = admission; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }
        public Builder doctor(Doctor doctor) { this.doctor = doctor; return this; }
        public Builder diagnosis(String diagnosis) { this.diagnosis = diagnosis; return this; }
        public Builder treatment(String treatment) { this.treatment = treatment; return this; }
        public Builder recordDate(LocalDate recordDate) { this.recordDate = recordDate; return this; }

        public MedicalRecord build() {
            MedicalRecord record = new MedicalRecord(admission, patient, doctor, diagnosis, treatment, recordDate);
            record.recordId = this.recordId;
            return record;
        }
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordId=" + recordId +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatment='" + treatment + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return recordId != null && recordId.equals(that.recordId);
    }

    @Override
    public int hashCode() {
        return recordId != null ? recordId.hashCode() : 0;
    }
}
