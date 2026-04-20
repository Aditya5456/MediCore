package com.hospital.dto;

public class DoctorResponseDTO {
    private Integer doctorId;
    private String  name;
    private String  specialization;
    private String  phone;
    private String  email;
    private Integer salary;
    private String  departmentName;
    private String  departmentLocation;

    // ── Constructors ─────────────────────────────────────────────────────────
    public DoctorResponseDTO() {}

    public DoctorResponseDTO(Integer doctorId, String name, String specialization, String phone, String email,
                              Integer salary, String departmentName, String departmentLocation) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.departmentName = departmentName;
        this.departmentLocation = departmentLocation;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

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

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getDepartmentLocation() { return departmentLocation; }
    public void setDepartmentLocation(String departmentLocation) { this.departmentLocation = departmentLocation; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer doctorId;
        private String name;
        private String specialization;
        private String phone;
        private String email;
        private Integer salary;
        private String departmentName;
        private String departmentLocation;

        public Builder doctorId(Integer doctorId) { this.doctorId = doctorId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder specialization(String specialization) { this.specialization = specialization; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder salary(Integer salary) { this.salary = salary; return this; }
        public Builder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public Builder departmentLocation(String departmentLocation) { this.departmentLocation = departmentLocation; return this; }

        public DoctorResponseDTO build() {
            return new DoctorResponseDTO(doctorId, name, specialization, phone, email, salary, departmentName, departmentLocation);
        }
    }

    @Override
    public String toString() {
        return "DoctorResponseDTO{" +
                "doctorId=" + doctorId +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", departmentName='" + departmentName + '\'' +
                ", departmentLocation='" + departmentLocation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorResponseDTO that = (DoctorResponseDTO) o;
        return doctorId != null && doctorId.equals(that.doctorId);
    }

    @Override
    public int hashCode() {
        return doctorId != null ? doctorId.hashCode() : 0;
    }
}
