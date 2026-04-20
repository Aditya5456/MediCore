package com.hospital.controller;

import com.hospital.config.ApiResponse;
import com.hospital.dto.DoctorRequestDTO;
import com.hospital.dto.DoctorResponseDTO;
import com.hospital.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  DoctorController  –  REST API for doctor management
 *
 *  Base URL:  /api/doctors
 *
 *  FULL API REFERENCE
 *  ──────────────────────────────────────────────────────────────────────────
 *  POST   /api/doctors                          → Add new doctor         (201)
 *  GET    /api/doctors                          → List all doctors       (200)
 *  GET    /api/doctors/{id}                     → Get doctor by ID       (200)
 *  GET    /api/doctors/search?name=             → Search by name         (200)
 *  GET    /api/doctors/department/{deptId}      → Doctors in dept        (200)
 *  GET    /api/doctors/top-salary               → Highest-paid doctors   (200)
 *  PUT    /api/doctors/{id}                     → Update doctor          (200)
 *  DELETE /api/doctors/{id}                     → Delete doctor          (200)
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // ── POST /api/doctors ─────────────────────────────────────────────────────
    /**
     * Add a new doctor to the system.
     *
     * Request body example:
     * {
     *   "name":           "Dr. Ramesh Kumar",
     *   "specialization": "Cardiology",
     *   "phone":          "9876543210",
     *   "email":          "ramesh@hms.com",
     *   "salary":         85000,
     *   "deptId":         1
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> addDoctor(
            @Valid @RequestBody DoctorRequestDTO request) {

        DoctorResponseDTO response = doctorService.addDoctor(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Doctor added successfully.", response));
    }

    // ── GET /api/doctors ──────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponseDTO>>> getAllDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(
                ApiResponse.ok("Fetched " + doctors.size() + " doctors.", doctors));
    }

    // ── GET /api/doctors/{id} ─────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> getDoctorById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(doctorService.getDoctorById(id)));
    }

    // ── GET /api/doctors/search?name= ─────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DoctorResponseDTO>>> searchDoctors(
            @RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.ok(doctorService.searchDoctors(name)));
    }

    // ── GET /api/doctors/department/{deptId} ──────────────────────────────────
    @GetMapping("/department/{deptId}")
    public ResponseEntity<ApiResponse<List<DoctorResponseDTO>>> getDoctorsByDepartment(
            @PathVariable Integer deptId) {
        return ResponseEntity.ok(
                ApiResponse.ok(doctorService.getDoctorsByDepartment(deptId)));
    }

    // ── GET /api/doctors/top-salary ───────────────────────────────────────────
    @GetMapping("/top-salary")
    public ResponseEntity<ApiResponse<List<DoctorResponseDTO>>> getTopSalaryDoctors() {
        return ResponseEntity.ok(
                ApiResponse.ok(doctorService.getDoctorsWithHighestSalary()));
    }

    // ── PUT /api/doctors/{id} ─────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> updateDoctor(
            @PathVariable Integer id,
            @Valid @RequestBody DoctorRequestDTO request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Doctor updated successfully.", doctorService.updateDoctor(id, request)));
    }

    // ── DELETE /api/doctors/{id} ──────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Integer id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.ok("Doctor deleted successfully."));
    }
}
