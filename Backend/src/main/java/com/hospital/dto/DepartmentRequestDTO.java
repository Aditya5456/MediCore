package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

// ════════════════════════════════════════════
//  DEPARTMENT
// ════════════════════════════════════════════

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DepartmentRequestDTO {

    @NotBlank(message = "Department name is required")
    @Size(max = 50)
    private String deptName;

    @NotBlank(message = "Location is required")
    @Size(max = 50)
    private String location;
}
