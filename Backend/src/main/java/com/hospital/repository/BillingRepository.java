package com.hospital.repository;

import com.hospital.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {

    List<Billing> findByPatient_PatientId(Integer patientId);
    List<Billing> findByPaymentStatus(String paymentStatus);
    List<Billing> findByAdmission_AdmissionId(Integer admissionId);

    /** Total revenue collected (sum of paid_amount across all bills). */
    @Query("SELECT COALESCE(SUM(b.paidAmount), 0) FROM Billing b")
    Long totalRevenue();

    /** Total billed but unpaid amount. */
    @Query("SELECT COALESCE(SUM(b.totalAmount - b.paidAmount), 0) FROM Billing b WHERE b.paymentStatus != 'Paid'")
    Long totalOutstanding();

    /** Bills grouped by payment status — dashboard breakdown. */
    @Query("""
           SELECT b.paymentStatus, COUNT(b), COALESCE(SUM(b.totalAmount), 0)
           FROM Billing b
           GROUP BY b.paymentStatus
           """)
    List<Object[]> revenueByStatus();

    /** Check if a bill already exists for a given admission (prevents duplicates). */
    boolean existsByAdmission_AdmissionId(Integer admissionId);

    /**
     * Patients whose total billed amount exceeds a threshold.
     * Mirrors Chapter 3 aggregate query:
     *   SELECT patient_id, SUM(total_amount) ... HAVING SUM > 7000
     */
    @Query("""
           SELECT b.patient.name, SUM(b.totalAmount)
           FROM Billing b
           GROUP BY b.patient.patientId, b.patient.name
           HAVING SUM(b.totalAmount) > :threshold
           ORDER BY SUM(b.totalAmount) DESC
           """)
    List<Object[]> findHighValuePatients(@Param("threshold") Integer threshold);
}
