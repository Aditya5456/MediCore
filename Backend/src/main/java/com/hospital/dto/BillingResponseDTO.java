package com.hospital.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingResponseDTO {
    private Integer   billId;
    private Integer   patientId;
    private String    patientName;
    private Integer   admissionId;
    private LocalDate billDate;
    private Integer   roomCharges;
    private Integer   medicineCharges;
    private Integer   doctorCharges;
    private Integer   totalAmount;
    private Integer   paidAmount;
    private String    paymentStatus;
}
