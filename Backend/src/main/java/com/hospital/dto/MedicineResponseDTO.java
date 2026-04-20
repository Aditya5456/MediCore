package com.hospital.dto;

import java.time.LocalDate;

public class MedicineResponseDTO {
    private Integer   medicineId;
    private String    name;
    private String    manufacturer;
    private Integer   unitPrice;
    private Integer   stockQty;
    private LocalDate expiryDate;
    private String    stockStatus;   // "In Stock" / "Low Stock" / "Out of Stock" — derived

    // ── Constructors ─────────────────────────────────────────────────────────
    public MedicineResponseDTO() {}

    public MedicineResponseDTO(Integer medicineId, String name, String manufacturer, Integer unitPrice,
                                Integer stockQty, LocalDate expiryDate, String stockStatus) {
        this.medicineId = medicineId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.stockQty = stockQty;
        this.expiryDate = expiryDate;
        this.stockStatus = stockStatus;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public Integer getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Integer unitPrice) { this.unitPrice = unitPrice; }

    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer medicineId;
        private String name;
        private String manufacturer;
        private Integer unitPrice;
        private Integer stockQty;
        private LocalDate expiryDate;
        private String stockStatus;

        public Builder medicineId(Integer medicineId) { this.medicineId = medicineId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder manufacturer(String manufacturer) { this.manufacturer = manufacturer; return this; }
        public Builder unitPrice(Integer unitPrice) { this.unitPrice = unitPrice; return this; }
        public Builder stockQty(Integer stockQty) { this.stockQty = stockQty; return this; }
        public Builder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder stockStatus(String stockStatus) { this.stockStatus = stockStatus; return this; }

        public MedicineResponseDTO build() {
            return new MedicineResponseDTO(medicineId, name, manufacturer, unitPrice, stockQty, expiryDate, stockStatus);
        }
    }

    @Override
    public String toString() {
        return "MedicineResponseDTO{" +
                "medicineId=" + medicineId +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", unitPrice=" + unitPrice +
                ", stockQty=" + stockQty +
                ", expiryDate=" + expiryDate +
                ", stockStatus='" + stockStatus + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicineResponseDTO that = (MedicineResponseDTO) o;
        return medicineId != null && medicineId.equals(that.medicineId);
    }

    @Override
    public int hashCode() {
        return medicineId != null ? medicineId.hashCode() : 0;
    }
}
