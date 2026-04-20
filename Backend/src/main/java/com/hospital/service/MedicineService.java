package com.hospital.service;

import com.hospital.dto.MedicineRequestDTO;
import com.hospital.dto.MedicineResponseDTO;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Medicine;
import com.hospital.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MedicineService {

    // Low-stock alert threshold (configurable)
    private static final int LOW_STOCK_THRESHOLD = 50;

    private final MedicineRepository medicineRepository;

    // ── CREATE ────────────────────────────────────────────────────────────────

    public MedicineResponseDTO addMedicine(MedicineRequestDTO request) {
        if (medicineRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Medicine", "name", request.getName());
        }
        Medicine medicine = Medicine.builder()
                .name(request.getName())
                .manufacturer(request.getManufacturer())
                .unitPrice(request.getUnitPrice())
                .stockQty(request.getStockQty() != null ? request.getStockQty() : 0)
                .expiryDate(request.getExpiryDate())
                .build();
        Medicine saved = medicineRepository.save(medicine);
        log.info("Medicine '{}' added with ID: {}", saved.getName(), saved.getMedicineId());
        return toResponseDTO(saved);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<MedicineResponseDTO> getAllMedicines() {
        return medicineRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicineResponseDTO getMedicineById(Integer id) {
        return toResponseDTO(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<MedicineResponseDTO> searchMedicines(String keyword) {
        return medicineRepository.findByNameContainingIgnoreCase(keyword)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicineResponseDTO> getLowStockMedicines() {
        return medicineRepository.findLowStockMedicines(LOW_STOCK_THRESHOLD)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicineResponseDTO> getExpiringSoon() {
        // Medicines expiring within the next 90 days
        LocalDate threshold = LocalDate.now().plusDays(90);
        return medicineRepository.findByExpiryDateBefore(threshold)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public MedicineResponseDTO updateMedicine(Integer id, MedicineRequestDTO request) {
        Medicine medicine = findOrThrow(id);

        if (!medicine.getName().equalsIgnoreCase(request.getName())
                && medicineRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Medicine", "name", request.getName());
        }
        medicine.setName(request.getName());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setUnitPrice(request.getUnitPrice());
        medicine.setStockQty(request.getStockQty() != null ? request.getStockQty() : medicine.getStockQty());
        medicine.setExpiryDate(request.getExpiryDate());
        return toResponseDTO(medicineRepository.save(medicine));
    }

    /**
     * Restock — adds quantity to existing stock.
     * Uses a separate endpoint so callers don't need to send the full medicine object.
     */
    public MedicineResponseDTO restockMedicine(Integer id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Restock quantity must be greater than zero.");
        }
        Medicine medicine = findOrThrow(id);
        medicine.setStockQty(medicine.getStockQty() + quantity);
        log.info("Medicine '{}' restocked by {}. New stock: {}",
                medicine.getName(), quantity, medicine.getStockQty());
        return toResponseDTO(medicineRepository.save(medicine));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void deleteMedicine(Integer id) {
        medicineRepository.delete(findOrThrow(id));
        log.warn("Medicine ID {} deleted.", id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Medicine findOrThrow(Integer id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", id));
    }

    /**
     * Derives a human-readable stock status label.
     * Out of Stock  → stockQty == 0
     * Low Stock     → stockQty <= LOW_STOCK_THRESHOLD
     * In Stock      → everything else
     */
    private String deriveStockStatus(int qty) {
        if (qty == 0)                    return "Out of Stock";
        if (qty <= LOW_STOCK_THRESHOLD)  return "Low Stock";
        return "In Stock";
    }

    private MedicineResponseDTO toResponseDTO(Medicine m) {
        return MedicineResponseDTO.builder()
                .medicineId(m.getMedicineId())
                .name(m.getName())
                .manufacturer(m.getManufacturer())
                .unitPrice(m.getUnitPrice())
                .stockQty(m.getStockQty())
                .expiryDate(m.getExpiryDate())
                .stockStatus(deriveStockStatus(m.getStockQty()))
                .build();
    }
}
