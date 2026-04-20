package com.hospital.service;

import com.hospital.dto.AppointmentRequestDTO;
import com.hospital.dto.AppointmentResponseDTO;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository     patientRepository;
    private final DoctorRepository      doctorRepository;

    // ── CREATE ────────────────────────────────────────────────────────────────

    public AppointmentResponseDTO bookAppointment(AppointmentRequestDTO request) {
        log.info("Booking appointment: patient={}, doctor={}, date={}",
                request.getPatientId(), request.getDoctorId(), request.getAppointmentDate());

        // Validate patient exists
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", request.getPatientId()));

        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));

        // Business rule: check for slot conflict (same doctor, same date + time)
        if (request.getAppointmentTime() != null
                && appointmentRepository.existsConflict(
                        request.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime())) {
            throw new BadRequestException(
                    String.format("Doctor '%s' already has an appointment at %s on %s.",
                            doctor.getName(),
                            request.getAppointmentTime(),
                            request.getAppointmentDate()));
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .notes(request.getNotes())
                .status("Scheduled")
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment booked with ID: {}", saved.getAppointmentId());
        return toResponseDTO(saved);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponseDTO getAppointmentById(Integer id) {
        return toResponseDTO(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getByPatient(Integer patientId) {
        if (!patientRepository.existsById(patientId))
            throw new ResourceNotFoundException("Patient", patientId);
        return appointmentRepository.findByPatient_PatientId(patientId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getByDoctor(Integer doctorId) {
        if (!doctorRepository.existsById(doctorId))
            throw new ResourceNotFoundException("Doctor", doctorId);
        return appointmentRepository.findByDoctor_DoctorId(doctorId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getTodaysAppointments() {
        return getByDate(LocalDate.now());
    }

    // ── UPDATE STATUS ─────────────────────────────────────────────────────────

    public AppointmentResponseDTO updateStatus(Integer id, String newStatus) {
        Appointment appt = findOrThrow(id);

        // Business rule: cannot reactivate a cancelled appointment
        if ("Cancelled".equals(appt.getStatus()) && !"Cancelled".equals(newStatus)) {
            throw new BadRequestException(
                    "Cannot change status of a Cancelled appointment. Please book a new one.");
        }
        // Business rule: cannot un-complete a completed appointment
        if ("Completed".equals(appt.getStatus()) && "Scheduled".equals(newStatus)) {
            throw new BadRequestException("Cannot move a Completed appointment back to Scheduled.");
        }

        appt.setStatus(newStatus);
        return toResponseDTO(appointmentRepository.save(appt));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void cancelAppointment(Integer id) {
        Appointment appt = findOrThrow(id);
        if ("Completed".equals(appt.getStatus())) {
            throw new BadRequestException("Cannot cancel a Completed appointment.");
        }
        appt.setStatus("Cancelled");
        appointmentRepository.save(appt);
        log.info("Appointment ID {} cancelled.", id);
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    private Appointment findOrThrow(Integer id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
    }

    private AppointmentResponseDTO toResponseDTO(Appointment a) {
        return AppointmentResponseDTO.builder()
                .appointmentId(a.getAppointmentId())
                .patientId(a.getPatient().getPatientId())
                .patientName(a.getPatient().getName())
                .doctorId(a.getDoctor().getDoctorId())
                .doctorName(a.getDoctor().getName())
                .doctorSpecialization(a.getDoctor().getSpecialization())
                .appointmentDate(a.getAppointmentDate())
                .appointmentTime(a.getAppointmentTime())
                .status(a.getStatus())
                .notes(a.getNotes())
                .build();
    }
}
