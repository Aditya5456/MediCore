package com.hospital.service;

import com.hospital.dto.DepartmentRequestDTO;
import com.hospital.dto.DepartmentResponseDTO;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Department;
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
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository     doctorRepository;

    public DepartmentResponseDTO addDepartment(DepartmentRequestDTO request) {
        if (departmentRepository.existsByDeptNameIgnoreCase(request.getDeptName())) {
            throw new DuplicateResourceException("Department", "name", request.getDeptName());
        }
        Department dept = Department.builder()
                .deptName(request.getDeptName())
                .location(request.getLocation())
                .build();
        return toResponseDTO(departmentRepository.save(dept));
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Integer id) {
        return toResponseDTO(findOrThrow(id));
    }

    public DepartmentResponseDTO updateDepartment(Integer id, DepartmentRequestDTO request) {
        Department dept = findOrThrow(id);

        // Allow same name on update (it's the same record)
        if (!dept.getDeptName().equalsIgnoreCase(request.getDeptName())
                && departmentRepository.existsByDeptNameIgnoreCase(request.getDeptName())) {
            throw new DuplicateResourceException("Department", "name", request.getDeptName());
        }
        dept.setDeptName(request.getDeptName());
        dept.setLocation(request.getLocation());
        return toResponseDTO(departmentRepository.save(dept));
    }

    public void deleteDepartment(Integer id) {
        Department dept = findOrThrow(id);
        // Guard: don't delete if doctors are still assigned
        long count = doctorRepository.findByDepartment_DeptId(id).size();
        if (count > 0) {
            throw new com.hospital.exception.BadRequestException(
                    "Cannot delete department '" + dept.getDeptName() +
                    "': " + count + " doctor(s) are still assigned to it.");
        }
        departmentRepository.delete(dept);
        log.warn("Department ID {} deleted.", id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Department findOrThrow(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }

    private DepartmentResponseDTO toResponseDTO(Department d) {
        // Count doctors in this department for the dashboard
        int doctorCount = doctorRepository.findByDepartment_DeptId(d.getDeptId()).size();
        return DepartmentResponseDTO.builder()
                .deptId(d.getDeptId())
                .deptName(d.getDeptName())
                .location(d.getLocation())
                .doctorCount(doctorCount)
                .build();
    }
}
