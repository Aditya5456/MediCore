package com.hospital.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {
    private Integer  patientId;
    private String   name;
    private LocalDate dob;
    private String   gender;
    private String   bloodGroup;
    private String   phone;
    private String   email;
    private String   address;
}
