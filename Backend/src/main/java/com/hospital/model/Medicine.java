package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Medicine inventory maintained by the hospital pharmacy.
 */
@Entity
@Table(name = "MEDICINE")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Integer medicineId;

    @NotBlank(message = "Medicine name is required")
    @Size(max = 100)
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Size(max = 100)
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be greater than zero")
    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @PositiveOrZero(message = "Stock quantity cannot be negative")
    @Column(name = "stock_qty")
    private Integer stockQty = 0;

    @Future(message = "Expiry date must be in the future")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    // ── Constructors ─────────────────────────────────────────────────────────
    public Medicine() {}

    public Medicine(String name, String manufacturer, Integer unitPrice, Integer stockQty, LocalDate expiryDate) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.stockQty = stockQty;
        this.expiryDate = expiryDate;
    }

    public Medicine(Integer medicineId, String name, String manufacturer, Integer unitPrice, Integer stockQty, LocalDate expiryDate) {
        this.medicineId = medicineId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.stockQty = stockQty;
        this.expiryDate = expiryDate;
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

    // ── Builder Pattern ──────────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer medicineId;
        private String name;
        private String manufacturer;
        private Integer unitPrice;
        private Integer stockQty = 0;
        private LocalDate expiryDate;

        public Builder medicineId(Integer medicineId) { this.medicineId = medicineId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder manufacturer(String manufacturer) { this.manufacturer = manufacturer; return this; }
        public Builder unitPrice(Integer unitPrice) { this.unitPrice = unitPrice; return this; }
        public Builder stockQty(Integer stockQty) { this.stockQty = stockQty; return this; }
        public Builder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }

        public Medicine build() {
            Medicine medicine = new Medicine(name, manufacturer, unitPrice, stockQty, expiryDate);
            medicine.medicineId = this.medicineId;
            return medicine;
        }
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", name='" + name + '\'' +
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
        Medicine medicine = (Medicine) o;
        return medicineId != null && medicineId.equals(medicine.medicineId);
    }

    @Override
    public int hashCode() {
        return medicineId != null ? medicineId.hashCode() : 0;
    }
}
