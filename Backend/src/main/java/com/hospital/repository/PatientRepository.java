package com.hospital.repository;

import com.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  PatientRepository
 *
 *  Extends JpaRepository<Patient, Integer> which gives us for FREE:
 *    save(entity)          → INSERT or UPDATE
 *    findById(id)          → SELECT WHERE patient_id = ?
 *    findAll()             → SELECT * FROM PATIENT
 *    deleteById(id)        → DELETE WHERE patient_id = ?
 *    existsById(id)        → SELECT COUNT(*) > 0
 *    count()               → SELECT COUNT(*)
 *    … and 15+ more methods
 *
 *  We add custom methods using:
 *    a) Spring Data query derivation  — method name → SQL  (no SQL needed)
 *    b) @Query(JPQL)                  — Java-style query using entity/field names
 *    c) @Query(nativeQuery=true)      — raw SQL for complex queries
 * ─────────────────────────────────────────────────────────────────────────────
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    // ── Spring Data Derived Queries ──────────────────────────────────────────
    // Spring reads the method name and generates the SQL automatically.

    /** SELECT * FROM PATIENT WHERE phone = ? */
    Optional<Patient> findByPhone(String phone);

    /** SELECT * FROM PATIENT WHERE email = ? */
    Optional<Patient> findByEmail(String email);

    /** SELECT COUNT(*) > 0 FROM PATIENT WHERE phone = ? */
    boolean existsByPhone(String phone);

    /** SELECT COUNT(*) > 0 FROM PATIENT WHERE email = ? */
    boolean existsByEmail(String email);

    /** SELECT * FROM PATIENT WHERE gender = ? */
    List<Patient> findByGender(String gender);

    /** SELECT * FROM PATIENT WHERE blood_group = ? */
    List<Patient> findByBloodGroup(String bloodGroup);

    /** SELECT * FROM PATIENT WHERE address LIKE %?% (case-insensitive city search) */
    List<Patient> findByAddressContainingIgnoreCase(String city);

    // ── JPQL Custom Queries ──────────────────────────────────────────────────
    // Uses entity class names (Patient) and field names (p.name), NOT table/column names.

    /**
     * Full-text search across name, phone, and email.
     * Useful for the search bar in the frontend.
     */
    @Query("""
           SELECT p FROM Patient p
           WHERE LOWER(p.name)  LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR p.phone        LIKE CONCAT('%', :keyword, '%')
              OR LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           ORDER BY p.name ASC
           """)
    List<Patient> searchPatients(@Param("keyword") String keyword);

    /**
     * Returns patients who currently have no appointments booked.
     * Equivalent to:  SELECT * FROM PATIENT p
     *                 WHERE p.patient_id NOT IN (SELECT patient_id FROM APPOINTMENT)
     */
    @Query("""
           SELECT p FROM Patient p
           WHERE p.patientId NOT IN (
               SELECT a.patient.patientId FROM Appointment a
           )
           """)
    List<Patient> findPatientsWithNoAppointments();

    // ── Native SQL Query ─────────────────────────────────────────────────────

    /**
     * Find patients with a 'Pending' billing status.
     * Using native SQL here to demonstrate the nativeQuery option.
     * Matches the query from Chapter 3 of the DBMS project doc.
     */
    @Query(value = """
           SELECT p.* FROM PATIENT p
           WHERE p.patient_id IN (
               SELECT b.patient_id FROM BILLING b
               WHERE b.payment_status = 'Pending'
           )
           """, nativeQuery = true)
    List<Patient> findPatientsWithPendingBills();
}
