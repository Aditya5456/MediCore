package com.hospital.service;

import com.hospital.dto.DoctorRequestDTO;
import com.hospital.dto.DoctorResponseDTO;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Department;
import com.hospital.model.Doctor;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
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
public class DoctorService {

    private final DoctorRepository      doctorRepository;
    private final DepartmentRepository  departmentRepository;

    // ── CREATE ────────────────────────────────────────────────────────────────

    public DoctorResponseDTO addDoctor(DoctorRequestDTO request) {
        log.info("Adding doctor: {}", request.getName());

        // Validate unique phone
        if (request.getPhone() != null && doctorRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Doctor", "phone", request.getPhone());
        }
        // Validate unique email
        if (request.getEmail() != null && doctorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Doctor", "email", request.getEmail());
        }

        // Resolve the department (throws 404 if not found)
        Department dept = departmentRepository.findById(request.getDeptId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", request.getDeptId()));

        Doctor doctor = Doctor.builder()
                .name(request.getName())
                .specialization(request.getSpecialization())
                .phone(request.getPhone())
                .email(request.getEmail())
                .salary(request.getSalary())
                .department(dept)
                .build();

        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor saved with ID: {}", saved.getDoctorId());
        return toResponseDTO(saved);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorById(Integer id) {
        return toResponseDTO(findDoctorOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getDoctorsByDepartment(Integer deptId) {
        // Ensure department exists first
        if (!departmentRepository.existsById(deptId)) {
            throw new ResourceNotFoundException("Department", deptId);
        }
        return doctorRepository.findByDepartment_DeptId(deptId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getDoctorsWithHighestSalary() {
        return doctorRepository.findDoctorsWithHighestSalary()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> searchDoctors(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public DoctorResponseDTO updateDoctor(Integer id, DoctorRequestDTO request) {
        Doctor doctor = findDoctorOrThrow(id);

        if (!doctor.getPhone().equals(request.getPhone())
                && doctorRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Doctor", "phone", request.getPhone());
        }

        Department dept = departmentRepository.findById(request.getDeptId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", request.getDeptId()));

        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setSalary(request.getSalary());
        doctor.setDepartment(dept);

        return toResponseDTO(doctorRepository.save(doctor));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void deleteDoctor(Integer id) {
        Doctor doctor = findDoctorOrThrow(id);
        log.warn("Deleting doctor ID: {}", id);
        doctorRepository.delete(doctor);
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    private Doctor findDoctorOrThrow(Integer id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
    }

    private DoctorResponseDTO toResponseDTO(Doctor d) {
        return DoctorResponseDTO.builder()
                .doctorId(d.getDoctorId())
                .name(d.getName())
                .specialization(d.getSpecialization())
                .phone(d.getPhone())
                .email(d.getEmail())
                .salary(d.getSalary())
                .departmentName(d.getDepartment() != null ? d.getDepartment().getDeptName() : null)
                .departmentLocation(d.getDepartment() != null ? d.getDepartment().getLocation() : null)
                .build();
    }
}
