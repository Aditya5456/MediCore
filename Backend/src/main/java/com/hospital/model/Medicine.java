package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Medicine inventory maintained by the hospital pharmacy.
 */
@Entity
@Table(name = "MEDICINE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private Integer stockQty = 0;

    @Future(message = "Expiry date must be in the future")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
}
