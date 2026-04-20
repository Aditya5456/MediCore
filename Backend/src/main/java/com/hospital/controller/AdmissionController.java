package com.hospital.controller;

import com.hospital.config.ApiResponse;
import com.hospital.dto.AdmissionRequestDTO;
import com.hospital.dto.AdmissionResponseDTO;
import com.hospital.service.AdmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  AdmissionController  –  REST API for patient admissions
 *
 *  Base URL:  /api/admissions
 *
 *  API REFERENCE
 *  ──────────────────────────────────────────────────────────────────────────
 *  POST   /api/admissions                       → Admit a patient          (201)
 *  GET    /api/admissions                       → All admissions           (200)
 *  GET    /api/admissions/{id}                  → Get by ID                (200)
 *  GET    /api/admissions/current               → Currently admitted       (200)
 *  GET    /api/admissions/patient/{patientId}   → By patient               (200)
 *  PATCH  /api/admissions/{id}/discharge        → Discharge patient        (200)
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/admissions")
@RequiredArgsConstructor
public class AdmissionController {

    private final AdmissionService admissionService;

    // ── POST /api/admissions ──────────────────────────────────────────────────
    /**
     * Admit a patient.
     *
     * Request body example:
     * {
     *   "patientId":  1,
     *   "doctorId":   1,
     *   "admitDate":  "2025-03-10"
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdmissionResponseDTO>> admitPatient(
            @Valid @RequestBody AdmissionRequestDTO request) {
        AdmissionResponseDTO response = admissionService.admitPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Patient admitted successfully.", response));
    }

    // ── GET /api/admissions ───────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdmissionResponseDTO>>> getAllAdmissions() {
        List<AdmissionResponseDTO> list = admissionService.getAllAdmissions();
        return ResponseEntity.ok(ApiResponse.ok("Fetched " + list.size() + " admissions.", list));
    }

    // ── GET /api/admissions/{id} ──────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdmissionResponseDTO>> getAdmissionById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(admissionService.getAdmissionById(id)));
    }

    // ── GET /api/admissions/current ───────────────────────────────────────────
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<List<AdmissionResponseDTO>>> getCurrentAdmissions() {
        List<AdmissionResponseDTO> list = admissionService.getCurrentAdmissions();
        return ResponseEntity.ok(
                ApiResponse.ok("Currently admitted: " + list.size() + " patient(s).", list));
    }

    // ── GET /api/admissions/patient/{patientId} ───────────────────────────────
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<AdmissionResponseDTO>>> getByPatient(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(ApiResponse.ok(admissionService.getAdmissionsByPatient(patientId)));
    }

    // ── PATCH /api/admissions/{id}/discharge ──────────────────────────────────
    /**
     * Discharge a patient.
     * Both dischargeDate and totalCharges must succeed — otherwise the
     * transaction rolls back entirely (Atomicity).
     *
     * Query params:
     *   dischargeDate  →  e.g. 2025-03-18
     *   totalCharges   →  e.g. 25000  (optional, defaults to 0)
     */
    @PatchMapping("/{id}/discharge")
    public ResponseEntity<ApiResponse<AdmissionResponseDTO>> dischargePatient(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dischargeDate,
            @RequestParam(required = false, defaultValue = "0") Integer totalCharges) {

        AdmissionResponseDTO response = admissionService.dischargePatient(id, dischargeDate, totalCharges);
        return ResponseEntity.ok(
                ApiResponse.ok("Patient discharged successfully.", response));
    }
}
