package com.hospital.dto;

import java.time.LocalDate;

public class BillingResponseDTO {
    private Integer   billId;
    private Integer   patientId;
    private String    patientName;
    private Integer   admissionId;
    private LocalDate billDate;
    private Integer   roomCharges;
    private Integer   medicineCharges;
    private Integer   doctorCharges;
    private Integer   totalAmount;
    private Integer   paidAmount;
    private String    paymentStatus;

    // ── Constructors ─────────────────────────────────────────────────────────
    public BillingResponseDTO() {}

    public BillingResponseDTO(Integer billId, Integer patientId, String patientName, Integer admissionId,
                               LocalDate billDate, Integer roomCharges, Integer medicineCharges,
                               Integer doctorCharges, Integer totalAmount, Integer paidAmount, String paymentStatus) {
        this.billId = billId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.admissionId = admissionId;
        this.billDate = billDate;
        this.roomCharges = roomCharges;
        this.medicineCharges = medicineCharges;
        this.doctorCharges = doctorCharges;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.paymentStatus = paymentStatus;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getBillId() { return billId; }
    public void setBillId(Integer billId) { this.billId = billId; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

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

    public Integer getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Integer totalAmount) { this.totalAmount = totalAmount; }

    public Integer getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Integer paidAmount) { this.paidAmount = paidAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer billId;
        private Integer patientId;
        private String patientName;
        private Integer admissionId;
        private LocalDate billDate;
        private Integer roomCharges;
        private Integer medicineCharges;
        private Integer doctorCharges;
        private Integer totalAmount;
        private Integer paidAmount;
        private String paymentStatus;

        public Builder billId(Integer billId) { this.billId = billId; return this; }
        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder admissionId(Integer admissionId) { this.admissionId = admissionId; return this; }
        public Builder billDate(LocalDate billDate) { this.billDate = billDate; return this; }
        public Builder roomCharges(Integer roomCharges) { this.roomCharges = roomCharges; return this; }
        public Builder medicineCharges(Integer medicineCharges) { this.medicineCharges = medicineCharges; return this; }
        public Builder doctorCharges(Integer doctorCharges) { this.doctorCharges = doctorCharges; return this; }
        public Builder totalAmount(Integer totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder paidAmount(Integer paidAmount) { this.paidAmount = paidAmount; return this; }
        public Builder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }

        public BillingResponseDTO build() {
            return new BillingResponseDTO(billId, patientId, patientName, admissionId, billDate, roomCharges,
                    medicineCharges, doctorCharges, totalAmount, paidAmount, paymentStatus);
        }
    }

    @Override
    public String toString() {
        return "BillingResponseDTO{" +
                "billId=" + billId +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", admissionId=" + admissionId +
                ", billDate=" + billDate +
                ", roomCharges=" + roomCharges +
                ", medicineCharges=" + medicineCharges +
                ", doctorCharges=" + doctorCharges +
                ", totalAmount=" + totalAmount +
                ", paidAmount=" + paidAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingResponseDTO that = (BillingResponseDTO) o;
        return billId != null && billId.equals(that.billId);
    }

    @Override
    public int hashCode() {
        return billId != null ? billId.hashCode() : 0;
    }
}
