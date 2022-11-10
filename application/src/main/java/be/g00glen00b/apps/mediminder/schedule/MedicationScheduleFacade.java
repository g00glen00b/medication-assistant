package be.g00glen00b.apps.mediminder.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface MedicationScheduleFacade {
    Page<MedicationScheduleDTO> findAll(UUID userId, Pageable pageable);

    MedicationScheduleDTO findById(UUID userId, UUID id);

    @Transactional
    MedicationScheduleDTO create(UUID userId, @Valid CreateMedicationScheduleRequestDTO request);

    @Transactional
    MedicationScheduleDTO update(UUID userId, UUID id, @Valid UpdateMedicationScheduleRequestDTO request);

    @Transactional
    void delete(UUID userId, UUID id);

    Collection<MedicationEventDTO> findEventsByDate(UUID userId, LocalDate date);

    @Transactional
    MedicationEventDTO completeEvent(UUID userId, UUID id, LocalDate eventDate);
}
