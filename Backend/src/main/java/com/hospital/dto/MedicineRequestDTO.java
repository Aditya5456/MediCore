package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
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
    @Builder.Default
    private Integer stockQty = 0;

    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;
}
