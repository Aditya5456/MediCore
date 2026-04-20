package com.hospital.controller;

import com.hospital.config.ApiResponse;
import com.hospital.dto.BillingRequestDTO;
import com.hospital.dto.BillingResponseDTO;
import com.hospital.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  BillingController  –  REST API for billing management
 *
 *  Base URL:  /api/billing
 *
 *  FULL API REFERENCE
 *  ──────────────────────────────────────────────────────────────────────────
 *  POST   /api/billing                          → Generate new bill       (201)
 *  GET    /api/billing                          → All bills               (200)
 *  GET    /api/billing/{id}                     → Get bill by ID          (200)
 *  GET    /api/billing/patient/{patientId}      → Bills for a patient     (200)
 *  GET    /api/billing/pending                  → All pending bills       (200)
 *  PATCH  /api/billing/{id}/payment?amount=     → Record a payment        (200)
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    // ── POST /api/billing ─────────────────────────────────────────────────────
    /**
     * Generate a new bill for an admission.
     * totalAmount is auto-computed from the three charge components.
     *
     * Request body example:
     * {
     *   "patientId":       1001,
     *   "admissionId":     3001,
     *   "billDate":        "2025-03-10",
     *   "roomCharges":     5000,
     *   "medicineCharges": 300,
     *   "doctorCharges":   2000,
     *   "paidAmount":      7300
     * }
     *
     * Response includes computed "totalAmount" and derived "paymentStatus".
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BillingResponseDTO>> generateBill(
            @Valid @RequestBody BillingRequestDTO request) {

        BillingResponseDTO response = billingService.generateBill(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Bill generated successfully.", response));
    }

    // ── GET /api/billing ──────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<BillingResponseDTO>>> getAllBills() {
        List<BillingResponseDTO> bills = billingService.getAllBills();
        return ResponseEntity.ok(
                ApiResponse.ok("Fetched " + bills.size() + " bills.", bills));
    }

    // ── GET /api/billing/{id} ─────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillingResponseDTO>> getBillById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(billingService.getBillById(id)));
    }

    // ── GET /api/billing/patient/{patientId} ──────────────────────────────────
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<BillingResponseDTO>>> getBillsByPatient(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(
                ApiResponse.ok(billingService.getBillsByPatient(patientId)));
    }

    // ── GET /api/billing/pending ──────────────────────────────────────────────
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<BillingResponseDTO>>> getPendingBills() {
        List<BillingResponseDTO> pending = billingService.getPendingBills();
        return ResponseEntity.ok(
                ApiResponse.ok("Pending bills: " + pending.size(), pending));
    }

    // ── PATCH /api/billing/{id}/payment?amount=5000 ───────────────────────────
    /**
     * Record a payment against an existing bill.
     * Payment is cumulative — multiple partial payments are supported.
     *
     * Example: GET /api/billing/7002/payment?amount=3000
     * Effect: adds ₹3000 to paid_amount, recalculates status → "Partial" or "Paid"
     */
    @PatchMapping("/{id}/payment")
    public ResponseEntity<ApiResponse<BillingResponseDTO>> recordPayment(
            @PathVariable Integer id,
            @RequestParam Integer amount) {

        BillingResponseDTO updated = billingService.recordPayment(id, amount);
        return ResponseEntity.ok(
                ApiResponse.ok("Payment of ₹" + amount + " recorded successfully.", updated));
    }
}
