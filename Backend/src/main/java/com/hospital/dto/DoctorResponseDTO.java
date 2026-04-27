package com.hospital.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDTO {
    private Integer doctorId;
    private String  name;
    private String  specialization;
    private String  phone;
    private String  email;
    private Integer salary;
    private String  departmentName;
    private String  departmentLocation;
}
