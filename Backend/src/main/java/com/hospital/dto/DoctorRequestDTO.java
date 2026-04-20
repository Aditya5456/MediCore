package com.hospital.dto;

import jakarta.validation.constraints.*;

// ─── Doctor Request DTO ─────────────────────────────────────────────────────

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

    // ── Constructors ─────────────────────────────────────────────────────────
    public DoctorRequestDTO() {}

    public DoctorRequestDTO(String name, String specialization, String phone, String email, Integer salary, Integer deptId) {
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.deptId = deptId;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getSalary() { return salary; }
    public void setSalary(Integer salary) { this.salary = salary; }

    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String specialization;
        private String phone;
        private String email;
        private Integer salary;
        private Integer deptId;

        public Builder name(String name) { this.name = name; return this; }
        public Builder specialization(String specialization) { this.specialization = specialization; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder salary(Integer salary) { this.salary = salary; return this; }
        public Builder deptId(Integer deptId) { this.deptId = deptId; return this; }

        public DoctorRequestDTO build() {
            return new DoctorRequestDTO(name, specialization, phone, email, salary, deptId);
        }
    }

    @Override
    public String toString() {
        return "DoctorRequestDTO{" +
                "name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", deptId=" + deptId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorRequestDTO that = (DoctorRequestDTO) o;
        return phone != null && phone.equals(that.phone);
    }

    @Override
    public int hashCode() {
        return phone != null ? phone.hashCode() : 0;
    }
}
