package be.g00glen00b.apps.medicationassistant.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, UUID> {
    Page<MedicationSchedule> findAllByUserId(UUID userId, Pageable pageable);
    Optional<MedicationSchedule> findByIdAndUserId(UUID id, UUID userId);
}
