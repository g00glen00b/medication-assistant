package be.g00glen00b.apps.mediminder.medication.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface MedicationEntityRepository extends JpaRepository<MedicationEntity, UUID> {
    Optional<MedicationEntity> findByNameIgnoreCase(String name);
    Page<MedicationEntity> findAllByNameContainingIgnoreCase(String partialName, Pageable pageable);
}
