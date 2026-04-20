package com.hospital.service;

import com.hospital.dto.BillingRequestDTO;
import com.hospital.dto.BillingResponseDTO;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Admission;
import com.hospital.model.Billing;
import com.hospital.model.Patient;
import com.hospital.repository.AdmissionRepository;
import com.hospital.repository.BillingRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BillingService {

    private final BillingRepository   billingRepository;
    private final PatientRepository   patientRepository;
    private final AdmissionRepository admissionRepository;

    // ── GENERATE BILL ─────────────────────────────────────────────────────────

    /**
     * Creates a new bill for a patient admission.
     *
     * Business rules enforced here:
     *  1. Patient and Admission must exist.
     *  2. Admission must belong to the given patient.
     *  3. A bill must not already exist for this admission (prevent duplicate billing).
     *  4. Paid amount cannot exceed total amount.
     *  5. Total amount is auto-computed: room + medicine + doctor.
     *  6. Payment status is derived from paid vs total.
     */
    public BillingResponseDTO generateBill(BillingRequestDTO request) {
        log.info("Generating bill for patient={}, admission={}",
                request.getPatientId(), request.getAdmissionId());

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", request.getPatientId()));

        Admission admission = admissionRepository.findById(request.getAdmissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Admission", request.getAdmissionId()));

        // Rule: Admission must belong to this patient
        if (!admission.getPatient().getPatientId().equals(request.getPatientId())) {
            throw new BadRequestException(
                    "Admission ID " + request.getAdmissionId() +
                    " does not belong to Patient ID " + request.getPatientId());
        }

        // Rule: Prevent duplicate bill for same admission
        if (billingRepository.existsByAdmission_AdmissionId(request.getAdmissionId())) {
            throw new BadRequestException(
                    "A bill has already been generated for Admission ID: " + request.getAdmissionId());
        }

        // Auto-compute total
        int room     = request.getRoomCharges()     != null ? request.getRoomCharges()     : 0;
        int medicine = request.getMedicineCharges() != null ? request.getMedicineCharges() : 0;
        int doctor   = request.getDoctorCharges()   != null ? request.getDoctorCharges()   : 0;
        int total    = room + medicine + doctor;
        int paid     = request.getPaidAmount()      != null ? request.getPaidAmount()       : 0;

        // Rule: paid cannot exceed total
        if (paid > total) {
            throw new BadRequestException(
                    String.format("Paid amount (₹%d) cannot exceed total amount (₹%d).", paid, total));
        }

        // Derive payment status
        String status = derivePaymentStatus(paid, total);

        Billing billing = Billing.builder()
                .patient(patient)
                .admission(admission)
                .billDate(request.getBillDate())
                .roomCharges(room)
                .medicineCharges(medicine)
                .doctorCharges(doctor)
                .totalAmount(total)
                .paidAmount(paid)
                .paymentStatus(status)
                .build();

        Billing saved = billingRepository.save(billing);
        log.info("Bill generated with ID: {}, total: ₹{}, status: {}", saved.getBillId(), total, status);
        return toResponseDTO(saved);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BillingResponseDTO> getAllBills() {
        return billingRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BillingResponseDTO getBillById(Integer id) {
        return toResponseDTO(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<BillingResponseDTO> getBillsByPatient(Integer patientId) {
        if (!patientRepository.existsById(patientId))
            throw new ResourceNotFoundException("Patient", patientId);
        return billingRepository.findByPatient_PatientId(patientId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillingResponseDTO> getPendingBills() {
        return billingRepository.findByPaymentStatus("Pending")
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ── UPDATE PAYMENT ────────────────────────────────────────────────────────

    /**
     * Records a new payment against an existing bill.
     * Adds the amount to existing paid amount (cumulative payment).
     * Re-derives status automatically.
     */
    public BillingResponseDTO recordPayment(Integer billId, Integer paymentAmount) {
        if (paymentAmount == null || paymentAmount <= 0) {
            throw new BadRequestException("Payment amount must be greater than zero.");
        }

        Billing bill = findOrThrow(billId);

        if ("Paid".equals(bill.getPaymentStatus())) {
            throw new BadRequestException("Bill ID " + billId + " is already fully paid.");
        }

        int newPaid = bill.getPaidAmount() + paymentAmount;
        if (newPaid > bill.getTotalAmount()) {
            throw new BadRequestException(
                    String.format("Payment of ₹%d would exceed total bill of ₹%d. Outstanding: ₹%d.",
                            paymentAmount,
                            bill.getTotalAmount(),
                            bill.getTotalAmount() - bill.getPaidAmount()));
        }

        bill.setPaidAmount(newPaid);
        bill.setPaymentStatus(derivePaymentStatus(newPaid, bill.getTotalAmount()));

        log.info("Payment of ₹{} recorded for Bill ID {}. New status: {}", paymentAmount, billId, bill.getPaymentStatus());
        return toResponseDTO(billingRepository.save(bill));
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    /**
     * Derives payment status from paid vs total amounts.
     * Single source of truth — used in both generateBill and recordPayment.
     */
    private String derivePaymentStatus(int paid, int total) {
        if (paid <= 0)       return "Pending";
        if (paid >= total)   return "Paid";
        return "Partial";
    }

    private Billing findOrThrow(Integer id) {
        return billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", id));
    }

    private BillingResponseDTO toResponseDTO(Billing b) {
        return BillingResponseDTO.builder()
                .billId(b.getBillId())
                .patientId(b.getPatient().getPatientId())
                .patientName(b.getPatient().getName())
                .admissionId(b.getAdmission().getAdmissionId())
                .billDate(b.getBillDate())
                .roomCharges(b.getRoomCharges())
                .medicineCharges(b.getMedicineCharges())
                .doctorCharges(b.getDoctorCharges())
                .totalAmount(b.getTotalAmount())
                .paidAmount(b.getPaidAmount())
                .paymentStatus(b.getPaymentStatus())
                .build();
    }
}
