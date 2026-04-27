package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;

/**
 * Financial bill generated for a patient admission.
 * total_amount = room_charges + medicine_charges + doctor_charges
 * Status lifecycle:  Pending → Partial → Paid
 */
@Entity
@Table(name = "BILLING")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private Integer roomCharges = 0;

    @PositiveOrZero
    @Column(name = "medicine_charges")
    @Builder.Default
    private Integer medicineCharges = 0;

    @PositiveOrZero
    @Column(name = "doctor_charges")
    @Builder.Default
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
    @Builder.Default
    private Integer paidAmount = 0;

    /**
     * Derived status set by service layer:
     *   paidAmount == 0              → "Pending"
     *   0 < paidAmount < totalAmount → "Partial"
     *   paidAmount >= totalAmount    → "Paid"
     */
    @Column(name = "payment_status", length = 20)
    @Builder.Default
    private String paymentStatus = "Pending";
}
