package com.hospital.service;

import com.hospital.dto.MedicalRecordRequestDTO;
import com.hospital.dto.MedicalRecordResponseDTO;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.*;
import com.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AdmissionRepository     admissionRepository;
    private final PatientRepository       patientRepository;
    private final DoctorRepository        doctorRepository;

    // ── CREATE ────────────────────────────────────────────────────────────────

    public MedicalRecordResponseDTO addRecord(MedicalRecordRequestDTO request) {
        Admission admission = admissionRepository.findById(request.getAdmissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Admission", request.getAdmissionId()));
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", request.getPatientId()));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));

        // Business rule: patient on the record must match the admission
        if (!admission.getPatient().getPatientId().equals(request.getPatientId())) {
            throw new com.hospital.exception.BadRequestException(
                    "Patient ID " + request.getPatientId() +
                    " does not match the patient on Admission #" + request.getAdmissionId());
        }

        MedicalRecord record = MedicalRecord.builder()
                .admission(admission)
                .patient(patient)
                .doctor(doctor)
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .recordDate(request.getRecordDate())
                .build();

        MedicalRecord saved = medicalRecordRepository.save(record);
        log.info("Medical record #{} created for patient '{}'.", saved.getRecordId(), patient.getName());
        return toResponseDTO(saved);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<MedicalRecordResponseDTO> getAllRecords() {
        return medicalRecordRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponseDTO getRecordById(Integer id) {
        return toResponseDTO(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponseDTO> getRecordsByPatient(Integer patientId) {
        if (!patientRepository.existsById(patientId))
            throw new ResourceNotFoundException("Patient", patientId);
        return medicalRecordRepository.findByPatient_PatientId(patientId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponseDTO> getRecordsByAdmission(Integer admissionId) {
        return medicalRecordRepository.findByAdmission_AdmissionId(admissionId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public MedicalRecordResponseDTO updateRecord(Integer id, MedicalRecordRequestDTO request) {
        MedicalRecord record = findOrThrow(id);
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setRecordDate(request.getRecordDate());
        return toResponseDTO(medicalRecordRepository.save(record));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void deleteRecord(Integer id) {
        medicalRecordRepository.delete(findOrThrow(id));
        log.warn("Medical record #{} deleted.", id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private MedicalRecord findOrThrow(Integer id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord", id));
    }

    private MedicalRecordResponseDTO toResponseDTO(MedicalRecord r) {
        return MedicalRecordResponseDTO.builder()
                .recordId(r.getRecordId())
                .admissionId(r.getAdmission().getAdmissionId())
                .patientId(r.getPatient().getPatientId())
                .patientName(r.getPatient().getName())
                .doctorId(r.getDoctor().getDoctorId())
                .doctorName(r.getDoctor().getName())
                .doctorSpecialization(r.getDoctor().getSpecialization())
                .diagnosis(r.getDiagnosis())
                .treatment(r.getTreatment())
                .recordDate(r.getRecordDate())
                .build();
    }
}
