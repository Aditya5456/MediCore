package com.hospital.repository;

import com.hospital.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // ── Derived queries ──────────────────────────────────────────────────────

    List<Appointment> findByPatient_PatientId(Integer patientId);
    List<Appointment> findByDoctor_DoctorId(Integer doctorId);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByAppointmentDate(LocalDate date);

    List<Appointment> findByAppointmentDateBetween(LocalDate from, LocalDate to);
    List<Appointment> findByDoctor_DoctorIdAndAppointmentDate(Integer doctorId, LocalDate date);

    /**
     * Count appointments per doctor — used for dashboard stats.
     */
    @Query("""
           SELECT a.doctor.name, COUNT(a)
           FROM Appointment a
           GROUP BY a.doctor.doctorId, a.doctor.name
           ORDER BY COUNT(a) DESC
           """)
    List<Object[]> countAppointmentsPerDoctor();

    /**
     * Count today's appointments by status — used for dashboard cards.
     */
    @Query("""
           SELECT a.status, COUNT(a)
           FROM Appointment a
           WHERE a.appointmentDate = :date
           GROUP BY a.status
           """)
    List<Object[]> countByStatusOnDate(@Param("date") LocalDate date);

    /**
     * Check for appointment slot conflict — same doctor, same date + time, not cancelled.
     */
    @Query("""
           SELECT COUNT(a) > 0 FROM Appointment a
           WHERE a.doctor.doctorId     = :doctorId
             AND a.appointmentDate     = :date
             AND a.appointmentTime     = :time
             AND a.status             != 'Cancelled'
           """)
    boolean existsConflict(@Param("doctorId") Integer doctorId,
                            @Param("date")     LocalDate date,
                            @Param("time")     String time);
}
