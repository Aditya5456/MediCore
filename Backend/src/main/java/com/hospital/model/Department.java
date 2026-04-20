package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Represents a hospital department (e.g. Cardiology, Neurology).
 * One Department → many Doctors.
 */
@Entity
@Table(name = "DEPARTMENT")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Integer deptId;

    @NotBlank(message = "Department name is required")
    @Size(max = 50)
    @Column(name = "dept_name", nullable = false, unique = true, length = 50)
    private String deptName;

    @NotBlank(message = "Location is required")
    @Size(max = 50)
    @Column(name = "location", nullable = false, length = 50)
    private String location;

    /**
     * Bidirectional mapping — one department has many doctors.
     * mappedBy = "department"  →  the 'department' field in Doctor owns the FK.
     * FetchType.LAZY           →  doctors are NOT loaded unless explicitly accessed
     *                             (prevents N+1 performance problems).
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Doctor> doctors;

    // ── Constructors ─────────────────────────────────────────────────────────
    public Department() {}

    public Department(String deptName, String location) {
        this.deptName = deptName;
        this.location = location;
    }

    public Department(Integer deptId, String deptName, String location) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.location = location;
    }

    public Department(Integer deptId, String deptName, String location, List<Doctor> doctors) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.location = location;
        this.doctors = doctors;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<Doctor> getDoctors() { return doctors; }
    public void setDoctors(List<Doctor> doctors) { this.doctors = doctors; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer deptId;
        private String deptName;
        private String location;
        private List<Doctor> doctors;

        public Builder deptId(Integer deptId) { this.deptId = deptId; return this; }
        public Builder deptName(String deptName) { this.deptName = deptName; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder doctors(List<Doctor> doctors) { this.doctors = doctors; return this; }

        public Department build() {
            return new Department(deptId, deptName, location, doctors);
        }
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return deptId != null && deptId.equals(that.deptId);
    }

    @Override
    public int hashCode() {
        return deptId != null ? deptId.hashCode() : 0;
    }
}
