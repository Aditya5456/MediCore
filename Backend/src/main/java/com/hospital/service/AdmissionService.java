package com.hospital.service;

import com.hospital.dto.AdmissionRequestDTO;
import com.hospital.dto.AdmissionResponseDTO;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Admission;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.repository.AdmissionRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final PatientRepository   patientRepository;
    private final DoctorRepository    doctorRepository;

    // ── ADMIT PATIENT ─────────────────────────────────────────────────────────

    public AdmissionResponseDTO admitPatient(AdmissionRequestDTO request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", request.getPatientId()));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));

        // Business rule: patient should not already be currently admitted
        boolean alreadyAdmitted = admissionRepository
                .findByPatient_PatientId(request.getPatientId())
                .stream()
                .anyMatch(a -> a.getDischargeDate() == null);
        if (alreadyAdmitted) {
            throw new BadRequestException(
                    "Patient '" + patient.getName() + "' is already admitted. Discharge them first.");
        }

        Admission admission = Admission.builder()
                .patient(patient)
                .doctor(doctor)
                .admitDate(request.getAdmitDate())
                .dischargeDate(null)
                .totalCharges(0)
                .build();

        Admission saved = admissionRepository.save(admission);
        log.info("Patient '{}' admitted. Admission ID: {}", patient.getName(), saved.getAdmissionId());
        return toResponseDTO(saved);
    }

    // ── DISCHARGE PATIENT ─────────────────────────────────────────────────────

    /**
     * Sets the discharge date on an existing admission.
     * Both the date and totalCharges update atomically — if either fails,
     * the whole transaction rolls back (Atomicity from Chapter 5).
     */
    public AdmissionResponseDTO dischargePatient(Integer admissionId, LocalDate dischargeDate,
                                                  Integer totalCharges) {
        Admission admission = findOrThrow(admissionId);

        if (admission.getDischargeDate() != null) {
            throw new BadRequestException(
                    "Admission #" + admissionId + " is already discharged on "
                    + admission.getDischargeDate() + ".");
        }
        if (dischargeDate.isBefore(admission.getAdmitDate())) {
            throw new BadRequestException(
                    "Discharge date cannot be before admit date (" + admission.getAdmitDate() + ").");
        }
        if (totalCharges != null && totalCharges < 0) {
            throw new BadRequestException("Total charges cannot be negative.");
        }

        admission.setDischargeDate(dischargeDate);
        if (totalCharges != null) admission.setTotalCharges(totalCharges);

        Admission saved = admissionRepository.save(admission);
        log.info("Admission #{} discharged on {}.", admissionId, dischargeDate);
        return toResponseDTO(saved);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AdmissionResponseDTO> getAllAdmissions() {
        return admissionRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdmissionResponseDTO getAdmissionById(Integer id) {
        return toResponseDTO(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<AdmissionResponseDTO> getCurrentAdmissions() {
        return admissionRepository.findByDischargeDateIsNull()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdmissionResponseDTO> getAdmissionsByPatient(Integer patientId) {
        if (!patientRepository.existsById(patientId))
            throw new ResourceNotFoundException("Patient", patientId);
        return admissionRepository.findByPatient_PatientId(patientId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Admission findOrThrow(Integer id) {
        return admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission", id));
    }

    private AdmissionResponseDTO toResponseDTO(Admission a) {
        return AdmissionResponseDTO.builder()
                .admissionId(a.getAdmissionId())
                .patientId(a.getPatient().getPatientId())
                .patientName(a.getPatient().getName())
                .doctorId(a.getDoctor().getDoctorId())
                .doctorName(a.getDoctor().getName())
                .doctorSpecialization(a.getDoctor().getSpecialization())
                .admitDate(a.getAdmitDate())
                .dischargeDate(a.getDischargeDate())
                .totalCharges(a.getTotalCharges())
                .status(a.getDischargeDate() == null ? "Admitted" : "Discharged")
                .build();
    }
}
