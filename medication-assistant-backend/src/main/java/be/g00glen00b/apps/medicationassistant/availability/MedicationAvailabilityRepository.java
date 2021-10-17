package be.g00glen00b.apps.medicationassistant.availability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

interface MedicationAvailabilityRepository extends JpaRepository<MedicationAvailability, UUID> {
    @Query("select a from MedicationAvailability a where a.userId = ?1")
    Page<MedicationAvailability> findAllByUserId(UUID userId, Pageable pageable);
    @Query("select a from MedicationAvailability a where a.id = ?1 and a.userId = ?2")
    Optional<MedicationAvailability> findByIdAndUserId(UUID id, UUID userId);
    @Query("select a from MedicationAvailability a where a.userId = ?1 and a.medicationId = ?2 and a.quantityTypeId = ?3 and a.quantity > 0 and (a.expiryDate is null or a.expiryDate >= ?4)")
    Page<MedicationAvailability> findAllNonEmptyNonExpiredByUserAndMedication(UUID userId, UUID medicationId, UUID quantityTypeId, LocalDate today, Pageable pageable);
}
