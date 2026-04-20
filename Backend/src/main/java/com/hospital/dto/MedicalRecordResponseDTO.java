package com.hospital.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicalRecordResponseDTO {
    private Integer   recordId;
    private Integer   admissionId;
    private Integer   patientId;
    private String    patientName;
    private Integer   doctorId;
    private String    doctorName;
    private String    doctorSpecialization;
    private String    diagnosis;
    private String    treatment;
    private LocalDate recordDate;
}
