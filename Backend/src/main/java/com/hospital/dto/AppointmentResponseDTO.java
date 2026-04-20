package com.hospital.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {
    private Integer   appointmentId;
    private Integer   patientId;
    private String    patientName;
    private Integer   doctorId;
    private String    doctorName;
    private String    doctorSpecialization;
    private LocalDate appointmentDate;
    private String    appointmentTime;
    private String    status;
    private String    notes;
}
