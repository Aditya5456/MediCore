package com.hospital.dto;

import java.time.LocalDate;

public class PatientResponseDTO {
    private Integer  patientId;
    private String   name;
    private LocalDate dob;
    private String   gender;
    private String   bloodGroup;
    private String   phone;
    private String   email;
    private String   address;

    // ── Constructors ─────────────────────────────────────────────────────────
    public PatientResponseDTO() {}

    public PatientResponseDTO(Integer patientId, String name, LocalDate dob, String gender, String bloodGroup,
                               String phone, String email, String address) {
        this.patientId = patientId;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

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

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer patientId;
        private String name;
        private LocalDate dob;
        private String gender;
        private String bloodGroup;
        private String phone;
        private String email;
        private String address;

        public Builder patientId(Integer patientId) { this.patientId = patientId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder dob(LocalDate dob) { this.dob = dob; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder bloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder address(String address) { this.address = address; return this; }

        public PatientResponseDTO build() {
            return new PatientResponseDTO(patientId, name, dob, gender, bloodGroup, phone, email, address);
        }
    }

    @Override
    public String toString() {
        return "PatientResponseDTO{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientResponseDTO that = (PatientResponseDTO) o;
        return patientId != null && patientId.equals(that.patientId);
    }

    @Override
    public int hashCode() {
        return patientId != null ? patientId.hashCode() : 0;
    }
}
