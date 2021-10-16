package be.g00glen00b.apps.medicationassistant.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.UUID;

public interface MedicationScheduleFacade {
    Page<MedicationScheduleDTO> findAllByUserId(UUID userId, Pageable pageable);
    MedicationScheduleDTO create(UUID userId, @Valid CreateMedicationScheduleRequestDTO input);
    void delete(UUID userId, UUID id);
}
