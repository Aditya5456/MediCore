package com.hospital.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicineResponseDTO {
    private Integer   medicineId;
    private String    name;
    private String    manufacturer;
    private Integer   unitPrice;
    private Integer   stockQty;
    private LocalDate expiryDate;
    private String    stockStatus;   // "In Stock" / "Low Stock" / "Out of Stock" — derived
}
