package com.hospital.repository;

import com.hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    // ── Derived queries ──────────────────────────────────────────────────────

    Optional<Doctor> findByPhone(String phone);
    Optional<Doctor> findByEmail(String email);

    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);

    /** All doctors in a specific department. */
    List<Doctor> findByDepartment_DeptId(Integer deptId);

    /** All doctors with a given specialization (case-insensitive). */
    List<Doctor> findBySpecializationIgnoreCase(String specialization);

    /** Doctors whose name contains the search string. */
    List<Doctor> findByNameContainingIgnoreCase(String name);

    // ── JPQL Queries ─────────────────────────────────────────────────────────

    /**
     * Doctors with the highest salary.
     * Mirrors the subquery from Chapter 3 of the project doc:
     *   SELECT name, salary FROM DOCTOR WHERE salary = (SELECT MAX(salary) FROM DOCTOR)
     */
    @Query("SELECT d FROM Doctor d WHERE d.salary = (SELECT MAX(d2.salary) FROM Doctor d2)")
    List<Doctor> findDoctorsWithHighestSalary();

    /**
     * Count of doctors grouped by department.
     * Returns Object[] { deptName, doctorCount }
     */
    @Query("""
           SELECT d.department.deptName, COUNT(d)
           FROM Doctor d
           GROUP BY d.department.deptName
           ORDER BY COUNT(d) DESC
           """)
    List<Object[]> countDoctorsByDepartment();

    /**
     * Doctors who have NO appointments booked.
     * Useful for finding available doctors.
     */
    @Query("""
           SELECT d FROM Doctor d
           WHERE d.doctorId NOT IN (
               SELECT a.doctor.doctorId FROM Appointment a
           )
           """)
    List<Doctor> findDoctorsWithNoAppointments();

    // ── Aggregate stats for dashboard ────────────────────────────────────────

    @Query("SELECT AVG(d.salary) FROM Doctor d")
    Double findAverageSalary();

    @Query("SELECT MAX(d.salary) FROM Doctor d")
    Integer findMaxSalary();

    @Query("SELECT MIN(d.salary) FROM Doctor d")
    Integer findMinSalary();
}
