package be.g00glen00b.apps.medicationassistant.medication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface MedicationRepository extends JpaRepository<Medication, UUID> {
    Optional<Medication> findByNameIgnoreCase(String name);
    Page<Medication> findAllByNameContainingIgnoreCase(String partialName, Pageable pageable);
}
