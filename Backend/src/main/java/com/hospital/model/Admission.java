package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Records a patient being admitted to the hospital under a doctor's care.
 * One-to-One with MedicalRecord and Billing.
 */
@Entity
@Table(name = "ADMISSION")
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admission_id")
    private Integer admissionId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull
    @Column(name = "admit_date", nullable = false)
    private LocalDate admitDate;

    // Null means patient is still admitted
    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Column(name = "total_charges")
    private Integer totalCharges = 0;

    // ── Constructors ─────────────────────────────────────────────────────────
    public Admission() {}

    public Admission(Patient patient, Doctor doctor, LocalDate admitDate, LocalDate dischargeDate, Integer totalCharges) {
        this.patient = patient;
        this.doctor = doctor;
        this.admitDate = admitDate;
        this.dischargeDate = dischargeDate;
        this.totalCharges = totalCharges;
    }

    public Admission(Integer admissionId, Patient patient, Doctor doctor, LocalDate admitDate,
                     LocalDate dischargeDate, Integer totalCharges) {
        this.admissionId = admissionId;
        this.patient = patient;
        this.doctor = doctor;
        this.admitDate = admitDate;
        this.dischargeDate = dischargeDate;
        this.totalCharges = totalCharges;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getAdmissionId() { return admissionId; }
    public void setAdmissionId(Integer admissionId) { this.admissionId = admissionId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public LocalDate getAdmitDate() { return admitDate; }
    public void setAdmitDate(LocalDate admitDate) { this.admitDate = admitDate; }

    public LocalDate getDischargeDate() { return dischargeDate; }
    public void setDischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; }

    public Integer getTotalCharges() { return totalCharges; }
    public void setTotalCharges(Integer totalCharges) { this.totalCharges = totalCharges; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer admissionId;
        private Patient patient;
        private Doctor doctor;
        private LocalDate admitDate;
        private LocalDate dischargeDate;
        private Integer totalCharges = 0;

        public Builder admissionId(Integer admissionId) { this.admissionId = admissionId; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }
        public Builder doctor(Doctor doctor) { this.doctor = doctor; return this; }
        public Builder admitDate(LocalDate admitDate) { this.admitDate = admitDate; return this; }
        public Builder dischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; return this; }
        public Builder totalCharges(Integer totalCharges) { this.totalCharges = totalCharges; return this; }

        public Admission build() {
            Admission admission = new Admission(patient, doctor, admitDate, dischargeDate, totalCharges);
            admission.admissionId = this.admissionId;
            return admission;
        }
    }

    @Override
    public String toString() {
        return "Admission{" +
                "admissionId=" + admissionId +
                ", admitDate=" + admitDate +
                ", dischargeDate=" + dischargeDate +
                ", totalCharges=" + totalCharges +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admission admission = (Admission) o;
        return admissionId != null && admissionId.equals(admission.admissionId);
    }

    @Override
    public int hashCode() {
        return admissionId != null ? admissionId.hashCode() : 0;
    }
}
