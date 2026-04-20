package com.hospital.repository;

import com.hospital.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {

    Optional<Medicine> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);

    List<Medicine> findByNameContainingIgnoreCase(String keyword);

    /** All medicines expiring before a given date — for pharmacy alerts. */
    List<Medicine> findByExpiryDateBefore(LocalDate date);

    /** Medicines with low stock (below a threshold). */
    @Query("SELECT m FROM Medicine m WHERE m.stockQty <= :threshold ORDER BY m.stockQty ASC")
    List<Medicine> findLowStockMedicines(@Param("threshold") Integer threshold);

    /** Medicines that are completely out of stock. */
    List<Medicine> findByStockQtyEquals(Integer qty);
}
