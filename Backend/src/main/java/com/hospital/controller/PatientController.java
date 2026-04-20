package com.hospital.controller;

import com.hospital.config.ApiResponse;
import com.hospital.dto.PatientRequestDTO;
import com.hospital.dto.PatientResponseDTO;
import com.hospital.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  PatientController  –  REST API for patient management
 *
 *  Base URL:  /api/patients
 *
 *  @RestController = @Controller + @ResponseBody
 *      → Every method returns JSON directly (no view templates)
 *
 *  @RequestMapping("/api/patients")
 *      → All endpoints in this class are prefixed with /api/patients
 *
 *  @Valid
 *      → Triggers Bean Validation on the @RequestBody.
 *        If validation fails, Spring throws MethodArgumentNotValidException
 *        which GlobalExceptionHandler converts to a clean 400 JSON response.
 * ─────────────────────────────────────────────────────────────────────────────
 *
 *  FULL API REFERENCE
 *  ──────────────────────────────────────────────────────────────────────────
 *  POST   /api/patients              → Register new patient          (201)
 *  GET    /api/patients              → List all patients             (200)
 *  GET    /api/patients/{id}         → Get patient by ID             (200)
 *  GET    /api/patients/phone/{ph}   → Get patient by phone          (200)
 *  GET    /api/patients/search?q=    → Search by name/phone/email    (200)
 *  GET    /api/patients/pending-bills→ Patients with pending bills   (200)
 *  PUT    /api/patients/{id}         → Full update of patient        (200)
 *  DELETE /api/patients/{id}         → Delete patient                (200)
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    // ── POST /api/patients ────────────────────────────────────────────────────
    /**
     * Register a new patient.
     * Returns HTTP 201 Created with the saved patient data.
     *
     * Request body example:
     * {
     *   "name":       "Arun Kumar",
     *   "dob":        "1990-06-15",
     *   "gender":     "Male",
     *   "bloodGroup": "B+",
     *   "phone":      "9845671230",
     *   "email":      "arun@gmail.com",
     *   "address":    "Chennai"
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponseDTO>> addPatient(
            @Valid @RequestBody PatientRequestDTO request) {

        PatientResponseDTO response = patientService.addPatient(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Patient registered successfully.", response));
    }

    // ── GET /api/patients ─────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> getAllPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(
                ApiResponse.ok("Fetched " + patients.size() + " patients.", patients));
    }

    // ── GET /api/patients/{id} ────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getPatientById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                ApiResponse.ok(patientService.getPatientById(id)));
    }

    // ── GET /api/patients/phone/{phone} ───────────────────────────────────────
    @GetMapping("/phone/{phone}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getPatientByPhone(
            @PathVariable String phone) {

        return ResponseEntity.ok(
                ApiResponse.ok(patientService.getPatientByPhone(phone)));
    }

    // ── GET /api/patients/search?q=keyword ───────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> searchPatients(
            @RequestParam(name = "q", required = false, defaultValue = "") String keyword) {

        List<PatientResponseDTO> results = patientService.searchPatients(keyword);
        return ResponseEntity.ok(
                ApiResponse.ok("Found " + results.size() + " result(s).", results));
    }

    // ── GET /api/patients/pending-bills ───────────────────────────────────────
    @GetMapping("/pending-bills")
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> getPatientsWithPendingBills() {
        return ResponseEntity.ok(
                ApiResponse.ok(patientService.getPatientsWithPendingBills()));
    }

    // ── PUT /api/patients/{id} ────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> updatePatient(
            @PathVariable Integer id,
            @Valid @RequestBody PatientRequestDTO request) {

        PatientResponseDTO updated = patientService.updatePatient(id, request);
        return ResponseEntity.ok(
                ApiResponse.ok("Patient updated successfully.", updated));
    }

    // ── DELETE /api/patients/{id} ─────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(
            @PathVariable Integer id) {

        patientService.deletePatient(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Patient deleted successfully."));
    }
}
