package be.g00glen00b.apps.mediminder.medication.implementation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface MedicationQuantityTypeEntityRepository extends JpaRepository<MedicationQuantityTypeEntity, UUID> {
}
