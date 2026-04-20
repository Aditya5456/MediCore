package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.*;

// ─── Doctor Request DTO ─────────────────────────────────────────────────────

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {

    @NotBlank(message = "Doctor name is required")
    @Size(max = 50)
    private String name;

    @NotBlank(message = "Specialization is required")
    @Size(max = 50)
    private String specialization;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit phone number")
    private String phone;

    @Email(message = "Enter a valid email address")
    private String email;

    @NotNull(message = "Salary is required")
    @Min(value = 30000, message = "Salary must be at least ₹30,000")
    private Integer salary;

    @NotNull(message = "Department ID is required")
    private Integer deptId;
}
