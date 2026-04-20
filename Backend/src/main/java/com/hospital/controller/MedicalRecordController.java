package com.hospital.controller;

import com.hospital.config.ApiResponse;
import com.hospital.dto.MedicalRecordRequestDTO;
import com.hospital.dto.MedicalRecordResponseDTO;
import com.hospital.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  MedicalRecordController  –  REST API for diagnosis and treatment records
 *
 *  Base URL:  /api/medical-records
 *
 *  API REFERENCE
 *  ──────────────────────────────────────────────────────────────────────────
 *  POST   /api/medical-records                      → Add record           (201)
 *  GET    /api/medical-records                      → All records          (200)
 *  GET    /api/medical-records/{id}                 → Get by ID            (200)
 *  GET    /api/medical-records/patient/{patientId}  → Patient history      (200)
 *  GET    /api/medical-records/admission/{admId}    → Records for admission(200)
 *  PUT    /api/medical-records/{id}                 → Update record        (200)
 *  DELETE /api/medical-records/{id}                 → Delete record        (200)
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicalRecordResponseDTO>> addRecord(
            @Valid @RequestBody MedicalRecordRequestDTO request) {
        MedicalRecordResponseDTO response = medicalRecordService.addRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Medical record created.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalRecordResponseDTO>>> getAllRecords() {
        List<MedicalRecordResponseDTO> list = medicalRecordService.getAllRecords();
        return ResponseEntity.ok(ApiResponse.ok("Fetched " + list.size() + " records.", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDTO>> getRecordById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(medicalRecordService.getRecordById(id)));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<MedicalRecordResponseDTO>>> getByPatient(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(ApiResponse.ok(medicalRecordService.getRecordsByPatient(patientId)));
    }

    @GetMapping("/admission/{admissionId}")
    public ResponseEntity<ApiResponse<List<MedicalRecordResponseDTO>>> getByAdmission(
            @PathVariable Integer admissionId) {
        return ResponseEntity.ok(ApiResponse.ok(medicalRecordService.getRecordsByAdmission(admissionId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDTO>> updateRecord(
            @PathVariable Integer id,
            @Valid @RequestBody MedicalRecordRequestDTO request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Medical record updated.", medicalRecordService.updateRecord(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable Integer id) {
        medicalRecordService.deleteRecord(id);
        return ResponseEntity.ok(ApiResponse.ok("Medical record deleted."));
    }
}
