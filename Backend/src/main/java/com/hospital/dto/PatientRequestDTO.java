package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

// ─── Patient Request DTO ────────────────────────────────────────────────────

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Pattern(regexp = "Male|Female|Other",
             message = "Gender must be Male, Female, or Other")
    private String gender;

    @Size(max = 5, message = "Blood group must not exceed 5 characters")
    private String bloodGroup;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$",
             message = "Enter a valid 10-digit Indian mobile number")
    private String phone;

    @Email(message = "Enter a valid email address")
    private String email;

    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;
}
