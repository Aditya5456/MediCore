package com.hospital.repository;

import com.hospital.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

    List<MedicalRecord> findByPatient_PatientId(Integer patientId);
    List<MedicalRecord> findByDoctor_DoctorId(Integer doctorId);
    List<MedicalRecord> findByAdmission_AdmissionId(Integer admissionId);

    /** A patient's most recent record — useful for "last visit" display. */
    Optional<MedicalRecord> findTopByPatient_PatientIdOrderByRecordDateDesc(Integer patientId);

    boolean existsByAdmission_AdmissionId(Integer admissionId);
}
