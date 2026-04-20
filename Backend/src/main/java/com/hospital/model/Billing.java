package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

/**
 * Financial bill generated for a patient admission.
 * total_amount = room_charges + medicine_charges + doctor_charges
 * Status lifecycle:  Pending → Partial → Paid
 */
@Entity
@Table(name = "BILLING")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Integer billId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @NotNull
    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate;

    @PositiveOrZero
    @Column(name = "room_charges")
    private Integer roomCharges = 0;

    @PositiveOrZero
    @Column(name = "medicine_charges")
    private Integer medicineCharges = 0;

    @PositiveOrZero
    @Column(name = "doctor_charges")
    private Integer doctorCharges = 0;

    /**
     * Computed and enforced in BillingService — not editable directly.
     * totalAmount = roomCharges + medicineCharges + doctorCharges
     */
    @NotNull
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @PositiveOrZero
    @Column(name = "paid_amount")
    private Integer paidAmount = 0;

    /**
     * Derived status set by service layer:
     *   paidAmount == 0              → "Pending"
     *   0 < paidAmount < totalAmount → "Partial"
     *   paidAmount >= totalAmount    → "Paid"
     */
    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "Pending";

    // ── Constructors ─────────────────────────────────────────────────────────
    public Billing() {}

    public Billing(Patient patient, Admission admission, LocalDate billDate, Integer roomCharges,
                   Integer medicineCharges, Integer doctorCharges, Integer totalAmount, Integer paidAmount,
                   String paymentStatus) {
        this.patient = patient;
        this.admission = admission;
        this.billDate = billDate;
        this.roomCharges = roomCharges;
        this.medicineCharges = medicineCharges;
        this.doctorCharges = doctorCharges;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.paymentStatus = paymentStatus;
    }

    public Billing(Integer billId, Patient patient, Admission admission, LocalDate billDate,
                   Integer roomCharges, Integer medicineCharges, Integer doctorCharges, Integer totalAmount,
                   Integer paidAmount, String paymentStatus) {
        this.billId = billId;
        this.patient = patient;
        this.admission = admission;
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

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Admission getAdmission() { return admission; }
    public void setAdmission(Admission admission) { this.admission = admission; }

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
        private Patient patient;
        private Admission admission;
        private LocalDate billDate;
        private Integer roomCharges = 0;
        private Integer medicineCharges = 0;
        private Integer doctorCharges = 0;
        private Integer totalAmount;
        private Integer paidAmount = 0;
        private String paymentStatus = "Pending";

        public Builder billId(Integer billId) { this.billId = billId; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }
        public Builder admission(Admission admission) { this.admission = admission; return this; }
        public Builder billDate(LocalDate billDate) { this.billDate = billDate; return this; }
        public Builder roomCharges(Integer roomCharges) { this.roomCharges = roomCharges; return this; }
        public Builder medicineCharges(Integer medicineCharges) { this.medicineCharges = medicineCharges; return this; }
        public Builder doctorCharges(Integer doctorCharges) { this.doctorCharges = doctorCharges; return this; }
        public Builder totalAmount(Integer totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder paidAmount(Integer paidAmount) { this.paidAmount = paidAmount; return this; }
        public Builder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }

        public Billing build() {
            Billing billing = new Billing(patient, admission, billDate, roomCharges, medicineCharges,
                    doctorCharges, totalAmount, paidAmount, paymentStatus);
            billing.billId = this.billId;
            return billing;
        }
    }

    @Override
    public String toString() {
        return "Billing{" +
                "billId=" + billId +
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
        Billing billing = (Billing) o;
        return billId != null && billId.equals(billing.billId);
    }

    @Override
    public int hashCode() {
        return billId != null ? billId.hashCode() : 0;
    }
}
