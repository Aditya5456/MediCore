package com.hospital.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class MedicineRequestDTO {

    @NotBlank(message = "Medicine name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String manufacturer;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be greater than zero")
    private Integer unitPrice;

    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQty = 0;

    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    // ── Constructors ─────────────────────────────────────────────────────────
    public MedicineRequestDTO() {}

    public MedicineRequestDTO(String name, String manufacturer, Integer unitPrice, Integer stockQty, LocalDate expiryDate) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.stockQty = stockQty;
        this.expiryDate = expiryDate;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
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

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String manufacturer;
        private Integer unitPrice;
        private Integer stockQty = 0;
        private LocalDate expiryDate;

        public Builder name(String name) { this.name = name; return this; }
        public Builder manufacturer(String manufacturer) { this.manufacturer = manufacturer; return this; }
        public Builder unitPrice(Integer unitPrice) { this.unitPrice = unitPrice; return this; }
        public Builder stockQty(Integer stockQty) { this.stockQty = stockQty; return this; }
        public Builder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }

        public MedicineRequestDTO build() {
            return new MedicineRequestDTO(name, manufacturer, unitPrice, stockQty, expiryDate);
        }
    }

    @Override
    public String toString() {
        return "MedicineRequestDTO{" +
                "name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", unitPrice=" + unitPrice +
                ", stockQty=" + stockQty +
                ", expiryDate=" + expiryDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicineRequestDTO that = (MedicineRequestDTO) o;
        return name != null && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
