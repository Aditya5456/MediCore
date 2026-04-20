package com.hospital.controller;

import com.hospital.config.ApiResponse;
import com.hospital.dto.AppointmentRequestDTO;
import com.hospital.dto.AppointmentResponseDTO;
import com.hospital.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  AppointmentController  –  REST API for appointment management
 *
 *  Base URL:  /api/appointments
 *
 *  FULL API REFERENCE
 *  ──────────────────────────────────────────────────────────────────────────
 *  POST   /api/appointments                    → Book appointment        (201)
 *  GET    /api/appointments                    → All appointments        (200)
 *  GET    /api/appointments/{id}               → Get by ID               (200)
 *  GET    /api/appointments/today              → Today's appointments    (200)
 *  GET    /api/appointments/date?date=         → By specific date        (200)
 *  GET    /api/appointments/patient/{pid}      → By patient              (200)
 *  GET    /api/appointments/doctor/{did}       → By doctor               (200)
 *  PATCH  /api/appointments/{id}/status        → Update status           (200)
 *  DELETE /api/appointments/{id}/cancel        → Cancel appointment      (200)
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // ── POST /api/appointments ────────────────────────────────────────────────
    /**
     * Book a new appointment.
     *
     * Request body example:
     * {
     *   "patientId":       1001,
     *   "doctorId":        101,
     *   "appointmentDate": "2025-04-15",
     *   "appointmentTime": "09:00 AM",
     *   "notes":           "Chest pain follow-up"
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> bookAppointment(
            @Valid @RequestBody AppointmentRequestDTO request) {

        AppointmentResponseDTO response = appointmentService.bookAppointment(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Appointment booked successfully.", response));
    }

    // ── GET /api/appointments ─────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getAllAppointments() {
        List<AppointmentResponseDTO> list = appointmentService.getAllAppointments();
        return ResponseEntity.ok(
                ApiResponse.ok("Fetched " + list.size() + " appointments.", list));
    }

    // ── GET /api/appointments/{id} ────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.getAppointmentById(id)));
    }

    // ── GET /api/appointments/today ───────────────────────────────────────────
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getTodaysAppointments() {
        List<AppointmentResponseDTO> list = appointmentService.getTodaysAppointments();
        return ResponseEntity.ok(
                ApiResponse.ok("Today's appointments: " + list.size(), list));
    }

    // ── GET /api/appointments/date?date=2025-03-10 ────────────────────────────
    @GetMapping("/date")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.getByDate(date)));
    }

    // ── GET /api/appointments/patient/{patientId} ─────────────────────────────
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getByPatient(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.getByPatient(patientId)));
    }

    // ── GET /api/appointments/doctor/{doctorId} ───────────────────────────────
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getByDoctor(
            @PathVariable Integer doctorId) {
        return ResponseEntity.ok(ApiResponse.ok(appointmentService.getByDoctor(doctorId)));
    }

    // ── PATCH /api/appointments/{id}/status ───────────────────────────────────
    /**
     * Update appointment status only (partial update — uses PATCH, not PUT).
     * Request body example:  { "status": "Completed" }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {

        AppointmentResponseDTO updated = appointmentService.updateStatus(id, status);
        return ResponseEntity.ok(
                ApiResponse.ok("Status updated to: " + status, updated));
    }

    // ── DELETE /api/appointments/{id}/cancel ──────────────────────────────────
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(@PathVariable Integer id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.ok("Appointment cancelled successfully."));
    }
}
