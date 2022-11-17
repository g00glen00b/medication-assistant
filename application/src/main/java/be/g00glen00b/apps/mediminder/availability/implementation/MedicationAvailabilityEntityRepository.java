package be.g00glen00b.apps.mediminder.availability.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface MedicationAvailabilityEntityRepository extends JpaRepository<MedicationAvailabilityEntity, UUID> {
    @Query("""
        select a from MedicationAvailabilityEntity a
        where a.userId = ?1
        and a.quantity > 0
        and a.expiryDate >= ?2
    """)
    Page<MedicationAvailabilityEntity> findAllNonEmptyNonExpiredByUserId(UUID userId, LocalDate today, Pageable pageable);
    Optional<MedicationAvailabilityEntity> findByIdAndUserId(UUID id, UUID userId);
    @Query("""
        select a from MedicationAvailabilityEntity a
        where a.userId = ?1
        and a.medicationId = ?2
        and a.quantity > 0
        and a.expiryDate >= ?3
    """)
    Page<MedicationAvailabilityEntity> findAllNonEmptyNonExpiredByUserIdAndMedicationId(UUID userId, UUID medicationId, LocalDate today, Pageable pageable);

    @Query("""
        select a from MedicationAvailabilityEntity a
        where a.quantity > 0
        and a.expiryDate <= ?1
    """)
    Page<MedicationAvailabilityEntity> findAllExpiredBeforeDate(LocalDate date, Pageable pageable);

    @Query("""
        select a.medicationId as medicationId, a.userId as userId, max(a.createdDate) as createdDate
         from MedicationAvailabilityEntity a
        where a.expiryDate >= ?2
        group by a.medicationId, a.userId
        having sum(a.quantity) / sum(a.initialQuantity) <= ?1
    """)
    Page<LowMedicationAvailabilityInfo> findAllMedicationIdsWithQuantityPercentageLessThan(BigDecimal percentage, LocalDate expiresAfterDate, Pageable pageable);

    @Query("""
        select a.medicationId as medicationId,
        sum(a.quantity) as totalQuantity,
        min(a.initialQuantity) as initialQuantityPerItem
        from MedicationAvailabilityEntity a
        where a.userId = ?1
        group by a.medicationId
    """)
    List<MedicationAvailabilityTotal> findTotalQuantityGroupedByMedicationByUserId(UUID userId);

}
