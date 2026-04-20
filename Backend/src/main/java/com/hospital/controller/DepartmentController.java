package com.hospital.controller;

import com.hospital.config.ApiResponse;
import com.hospital.dto.DepartmentRequestDTO;
import com.hospital.dto.DepartmentResponseDTO;
import com.hospital.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  DepartmentController  –  REST API for hospital departments
 *
 *  Base URL:  /api/departments
 *
 *  API REFERENCE
 *  ──────────────────────────────────────────────────────────────────────────
 *  POST   /api/departments         → Create a department       (201)
 *  GET    /api/departments         → List all departments      (200)
 *  GET    /api/departments/{id}    → Get department by ID      (200)
 *  PUT    /api/departments/{id}    → Update department         (200)
 *  DELETE /api/departments/{id}    → Delete department         (200)
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> addDepartment(
            @Valid @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO response = departmentService.addDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Department created successfully.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
        List<DepartmentResponseDTO> list = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.ok("Fetched " + list.size() + " departments.", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(departmentService.getDepartmentById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody DepartmentRequestDTO request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Department updated.", departmentService.updateDepartment(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.ok("Department deleted."));
    }
}
