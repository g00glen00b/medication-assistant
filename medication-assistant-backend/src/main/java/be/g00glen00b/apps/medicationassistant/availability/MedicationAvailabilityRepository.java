package be.g00glen00b.apps.medicationassistant.availability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

interface MedicationAvailabilityRepository extends JpaRepository<MedicationAvailability, UUID> {
    @Query("select a from MedicationAvailability a where a.userId = ?1")
    Page<MedicationAvailability> findAllByUserId(UUID userId, Pageable pageable);
    @Query("select a from MedicationAvailability a where a.id = ?1 and a.userId = ?2")
    Optional<MedicationAvailability> findByIdAndUserId(UUID id, UUID userId);
}
