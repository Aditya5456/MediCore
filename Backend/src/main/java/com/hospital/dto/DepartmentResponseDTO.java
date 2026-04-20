package com.hospital.dto;

public class DepartmentResponseDTO {
    private Integer deptId;
    private String  deptName;
    private String  location;
    private Integer doctorCount;   // convenience field — how many doctors are in this dept

    // ── Constructors ─────────────────────────────────────────────────────────
    public DepartmentResponseDTO() {}

    public DepartmentResponseDTO(Integer deptId, String deptName, String location, Integer doctorCount) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.location = location;
        this.doctorCount = doctorCount;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getDoctorCount() { return doctorCount; }
    public void setDoctorCount(Integer doctorCount) { this.doctorCount = doctorCount; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer deptId;
        private String deptName;
        private String location;
        private Integer doctorCount;

        public Builder deptId(Integer deptId) { this.deptId = deptId; return this; }
        public Builder deptName(String deptName) { this.deptName = deptName; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder doctorCount(Integer doctorCount) { this.doctorCount = doctorCount; return this; }

        public DepartmentResponseDTO build() {
            return new DepartmentResponseDTO(deptId, deptName, location, doctorCount);
        }
    }

    @Override
    public String toString() {
        return "DepartmentResponseDTO{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", location='" + location + '\'' +
                ", doctorCount=" + doctorCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentResponseDTO that = (DepartmentResponseDTO) o;
        return deptId != null && deptId.equals(that.deptId);
    }

    @Override
    public int hashCode() {
        return deptId != null ? deptId.hashCode() : 0;
    }
}
