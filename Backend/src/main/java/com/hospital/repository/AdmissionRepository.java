package com.hospital.repository;

import com.hospital.model.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Integer> {

    List<Admission> findByPatient_PatientId(Integer patientId);

    /** Currently admitted patients (discharge date is NULL). */
    List<Admission> findByDischargeDateIsNull();

    /** Patients admitted under a specific doctor. */
    List<Admission> findByDoctor_DoctorId(Integer doctorId);

    @Query("SELECT COUNT(a) FROM Admission a WHERE a.dischargeDate IS NULL")
    Long countCurrentlyAdmitted();
}
