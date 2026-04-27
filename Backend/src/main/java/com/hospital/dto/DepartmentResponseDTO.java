package com.hospital.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DepartmentResponseDTO {
    private Integer deptId;
    private String  deptName;
    private String  location;
    private Integer doctorCount;   // convenience field — how many doctors are in this dept
}
