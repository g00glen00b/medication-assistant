package be.g00glen00b.apps.medicationassistant.medication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface MedicationQuantityTypeRepository extends JpaRepository<MedicationQuantityType, UUID> {
    Optional<MedicationQuantityType> findByNameIgnoreCase(String name);
}
