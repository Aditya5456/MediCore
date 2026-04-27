package com.hospital.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AdmissionResponseDTO {
    private Integer   admissionId;
    private Integer   patientId;
    private String    patientName;
    private Integer   doctorId;
    private String    doctorName;
    private String    doctorSpecialization;
    private LocalDate admitDate;
    private LocalDate dischargeDate;
    private Integer   totalCharges;
    private String    status;      // "Admitted" or "Discharged" — derived field
}
