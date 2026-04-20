package com.hospital.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

// ─── Patient Request DTO ────────────────────────────────────────────────────

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

    // ── Constructors ───────────────────────────────────────────────────────────
    public PatientRequestDTO() {}

    public PatientRequestDTO(String name, LocalDate dob, String gender, String bloodGroup, String phone, String email, String address) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // ── Builder pattern ───────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private LocalDate dob;
        private String gender;
        private String bloodGroup;
        private String phone;
        private String email;
        private String address;

        public Builder name(String name) { this.name = name; return this; }
        public Builder dob(LocalDate dob) { this.dob = dob; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder bloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder address(String address) { this.address = address; return this; }

        public PatientRequestDTO build() {
            return new PatientRequestDTO(name, dob, gender, bloodGroup, phone, email, address);
        }
    }
}
