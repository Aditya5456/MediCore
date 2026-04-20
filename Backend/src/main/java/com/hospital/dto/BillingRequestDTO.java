package com.hospital.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

// ─── Billing Request DTO ─────────────────────────────────────────────────────

public class BillingRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    @NotNull(message = "Admission ID is required")
    private Integer admissionId;

    @NotNull(message = "Bill date is required")
    private LocalDate billDate;

    @PositiveOrZero(message = "Room charges cannot be negative")
    private Integer roomCharges = 0;

    @PositiveOrZero(message = "Medicine charges cannot be negative")
    private Integer medicineCharges = 0;

    @PositiveOrZero(message = "Doctor charges cannot be negative")
    private Integer doctorCharges = 0;

    @PositiveOrZero(message = "Paid amount cannot be negative")
    private Integer paidAmount = 0;

    // ── Constructors ─────────────────────────────────────────────────────────
    public BillingRequestDTO() {}

    public BillingRequestDTO(Integer patientId, Integer admissionId, LocalDate billDate, Integer roomCharges,
                              Integer medicineCharges, Integer doctorCharges, Integer paidAmount) {
        this.patientId = patientId;
        this.admissionId = admissionId;
        this.billDate = billDate;
        this.roomCharges = roomCharges;
        this.medicineCharges = medicineCharges;
        this.doctorCharges = doctorCharges;
        this.paidAmount = paidAmount;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public Integer getAdmissionId() { return admissionId; }
    public void setAdmissionId(Integer admissionId) { this.admissionId = admissionId; }

    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }

    public Integer getRoomCharges() { return roomCharges; }
    public void setRoomCharges(Integer roomCharges) { this.roomCharges = roomCharges; }

    public Integer getMedicineCharges() { return medicineCharges; }
    public void setMedicineCharges(Integer medicineCharges) { this.medicineCharges = medicineCharges; }

    public Integer getDoctorCharges() { return doctorCharges; }
    public void setDoctorCharges(Integer doctorCharges) { this.doctorCharges = doctorCharges; }

    public Integer getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Integer paidAmount) { this.paidAmount = paidAmount; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer patientId;
        private Integer admissionId;
        private LocalDate billDate;
        private Integer roomCharges = 0;
        private Integer medicineCharges = 0;
        private Integer doctorCharges = 0;
        private Integer paidAmount = 0;

        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder admissionId(Integer admissionId) { this.admissionId = admissionId; return this; }
        public Builder billDate(LocalDate billDate) { this.billDate = billDate; return this; }
        public Builder roomCharges(Integer roomCharges) { this.roomCharges = roomCharges; return this; }
        public Builder medicineCharges(Integer medicineCharges) { this.medicineCharges = medicineCharges; return this; }
        public Builder doctorCharges(Integer doctorCharges) { this.doctorCharges = doctorCharges; return this; }
        public Builder paidAmount(Integer paidAmount) { this.paidAmount = paidAmount; return this; }

        public BillingRequestDTO build() {
            return new BillingRequestDTO(patientId, admissionId, billDate, roomCharges, medicineCharges, doctorCharges, paidAmount);
        }
    }

    @Override
    public String toString() {
        return "BillingRequestDTO{" +
                "patientId=" + patientId +
                ", admissionId=" + admissionId +
                ", billDate=" + billDate +
                ", roomCharges=" + roomCharges +
                ", medicineCharges=" + medicineCharges +
                ", doctorCharges=" + doctorCharges +
                ", paidAmount=" + paidAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingRequestDTO that = (BillingRequestDTO) o;
        return patientId != null && patientId.equals(that.patientId) &&
               admissionId != null && admissionId.equals(that.admissionId) &&
               billDate != null && billDate.equals(that.billDate);
    }

    @Override
    public int hashCode() {
        int result = patientId != null ? patientId.hashCode() : 0;
        result = 31 * result + (admissionId != null ? admissionId.hashCode() : 0);
        result = 31 * result + (billDate != null ? billDate.hashCode() : 0);
        return result;
    }
}
