package com.hospital.controller;

import com.hospital.config.ApiResponse;
import com.hospital.dto.MedicineRequestDTO;
import com.hospital.dto.MedicineResponseDTO;
import com.hospital.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  MedicineController  –  REST API for pharmacy inventory
 *
 *  Base URL:  /api/medicines
 *
 *  API REFERENCE
 *  ──────────────────────────────────────────────────────────────────────────
 *  POST   /api/medicines                    → Add a medicine          (201)
 *  GET    /api/medicines                    → List all medicines      (200)
 *  GET    /api/medicines/{id}               → Get by ID               (200)
 *  GET    /api/medicines/search?q=          → Search by name          (200)
 *  GET    /api/medicines/low-stock          → Low or out-of-stock     (200)
 *  GET    /api/medicines/expiring-soon      → Expiring within 90 days (200)
 *  PUT    /api/medicines/{id}               → Update medicine details  (200)
 *  PATCH  /api/medicines/{id}/restock?qty=  → Add stock quantity      (200)
 *  DELETE /api/medicines/{id}               → Delete medicine         (200)
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicineResponseDTO>> addMedicine(
            @Valid @RequestBody MedicineRequestDTO request) {
        MedicineResponseDTO response = medicineService.addMedicine(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Medicine added successfully.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicineResponseDTO>>> getAllMedicines() {
        List<MedicineResponseDTO> list = medicineService.getAllMedicines();
        return ResponseEntity.ok(ApiResponse.ok("Fetched " + list.size() + " medicines.", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponseDTO>> getMedicineById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(medicineService.getMedicineById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MedicineResponseDTO>>> searchMedicines(
            @RequestParam(name = "q") String keyword) {
        return ResponseEntity.ok(ApiResponse.ok(medicineService.searchMedicines(keyword)));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<MedicineResponseDTO>>> getLowStock() {
        List<MedicineResponseDTO> list = medicineService.getLowStockMedicines();
        return ResponseEntity.ok(
                ApiResponse.ok("Low/out-of-stock medicines: " + list.size(), list));
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<ApiResponse<List<MedicineResponseDTO>>> getExpiringSoon() {
        List<MedicineResponseDTO> list = medicineService.getExpiringSoon();
        return ResponseEntity.ok(
                ApiResponse.ok("Medicines expiring within 90 days: " + list.size(), list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponseDTO>> updateMedicine(
            @PathVariable Integer id,
            @Valid @RequestBody MedicineRequestDTO request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Medicine updated.", medicineService.updateMedicine(id, request)));
    }

    // ── PATCH /api/medicines/{id}/restock?qty=100 ─────────────────────────────
    @PatchMapping("/{id}/restock")
    public ResponseEntity<ApiResponse<MedicineResponseDTO>> restockMedicine(
            @PathVariable Integer id,
            @RequestParam Integer qty) {
        return ResponseEntity.ok(
                ApiResponse.ok("Stock updated.", medicineService.restockMedicine(id, qty)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(@PathVariable Integer id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok(ApiResponse.ok("Medicine deleted."));
    }
}
