package com.hospital.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

// ════════════════════════════════════════════
//  DEPARTMENT
// ════════════════════════════════════════════

public class DepartmentRequestDTO {

    @NotBlank(message = "Department name is required")
    @Size(max = 50)
    private String deptName;

    @NotBlank(message = "Location is required")
    @Size(max = 50)
    private String location;

    // ── Constructors ─────────────────────────────────────────────────────────
    public DepartmentRequestDTO() {}

    public DepartmentRequestDTO(String deptName, String location) {
        this.deptName = deptName;
        this.location = location;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String deptName;
        private String location;

        public Builder deptName(String deptName) { this.deptName = deptName; return this; }
        public Builder location(String location) { this.location = location; return this; }

        public DepartmentRequestDTO build() {
            return new DepartmentRequestDTO(deptName, location);
        }
    }

    @Override
    public String toString() {
        return "DepartmentRequestDTO{" +
                "deptName='" + deptName + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentRequestDTO that = (DepartmentRequestDTO) o;
        return deptName != null && deptName.equals(that.deptName);
    }

    @Override
    public int hashCode() {
        return deptName != null ? deptName.hashCode() : 0;
    }
}
