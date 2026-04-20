package com.hospital.repository;

import com.hospital.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDeptNameIgnoreCase(String deptName);
    boolean existsByDeptNameIgnoreCase(String deptName);
}
