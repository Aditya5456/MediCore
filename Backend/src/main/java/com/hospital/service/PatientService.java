package com.hospital.service;

import com.hospital.dto.PatientRequestDTO;
import com.hospital.dto.PatientResponseDTO;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Patient;
import com.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  PatientService
 *
 *  Responsibilities:
 *    1. Business validation (duplicate phone/email, required fields)
 *    2. Mapping:  RequestDTO → Entity  and  Entity → ResponseDTO
 *    3. Calling the repository (database operations)
 *    4. Transaction management (@Transactional)
 *
 *  @Service    → marks this as a Spring-managed bean (auto-detected)
 *  @Transactional → wraps each public method in a DB transaction automatically.
 *                   If any exception occurs mid-method, the whole transaction
 *                   rolls back — keeping the DB consistent.
 * ─────────────────────────────────────────────────────────────────────────────
 */
@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    /**
     * Registers a new patient.
     * Checks for duplicate phone and email before saving.
     */
    public PatientResponseDTO addPatient(PatientRequestDTO request) {
        System.out.println("Registering new patient: " + request.getName());

        // Business rule: phone number must be unique
        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Patient", "phone", request.getPhone());
        }

        // Business rule: email must be unique (only if provided)
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Patient", "email", request.getEmail());
        }

        // Map DTO → Entity
        Patient patient = Patient.builder()
                .name(request.getName())
                .dob(request.getDob())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();

        // Save to DB (JPA handles the INSERT)
        Patient saved = patientRepository.save(patient);
        System.out.println("Patient registered with ID: " + saved.getPatientId());

        return toResponseDTO(saved);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    /**
     * Fetch all patients.
     * @Transactional(readOnly = true) tells Hibernate to optimise the session
     * for reads — no dirty checking, slightly better performance.
     */
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(Integer id) {
        Patient patient = findPatientOrThrow(id);
        return toResponseDTO(patient);
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientByPhone(String phone) {
        Patient patient = patientRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "phone", phone));
        return toResponseDTO(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDTO> searchPatients(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllPatients();
        }
        return patientRepository.searchPatients(keyword.trim())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getPatientsWithPendingBills() {
        return patientRepository.findPatientsWithPendingBills()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public PatientResponseDTO updatePatient(Integer id, PatientRequestDTO request) {
        Patient patient = findPatientOrThrow(id);
        System.out.println("Updating patient ID: " + id);

        // If phone is being changed, check it's not taken by someone else
        if (!patient.getPhone().equals(request.getPhone())
                && patientRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Patient", "phone", request.getPhone());
        }

        // Update fields
        patient.setName(request.getName());
        patient.setDob(request.getDob());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());

        // save() on an existing entity → JPA fires an UPDATE
        Patient updated = patientRepository.save(patient);
        return toResponseDTO(updated);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void deletePatient(Integer id) {
        Patient patient = findPatientOrThrow(id);
        System.out.println("Deleting patient ID: " + id + " — " + patient.getName());
        patientRepository.delete(patient);
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    /**
     * Reusable: fetch patient by ID or throw a clean 404.
     * Keeps every method DRY — one place to change the error message.
     */
    private Patient findPatientOrThrow(Integer id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", id));
    }

    /**
     * Maps a Patient entity → PatientResponseDTO.
     * Keeps entity fields out of the JSON response (e.g. the appointments list).
     */
    private PatientResponseDTO toResponseDTO(Patient p) {
        return PatientResponseDTO.builder()
                .patientId(p.getPatientId())
                .name(p.getName())
                .dob(p.getDob())
                .gender(p.getGender())
                .bloodGroup(p.getBloodGroup())
                .phone(p.getPhone())
                .email(p.getEmail())
                .address(p.getAddress())
                .build();
    }
}
