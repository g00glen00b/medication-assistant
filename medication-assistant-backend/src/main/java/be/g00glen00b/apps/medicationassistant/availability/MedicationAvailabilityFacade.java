package be.g00glen00b.apps.medicationassistant.availability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MedicationAvailabilityFacade {
    Page<MedicationAvailabilityDTO> findAllByUserId(UUID userId, Pageable pageable);
    MedicationAvailabilityDTO updateAvailability(UUID userId, UUID medicationId, MedicationAvailabilityInputDTO input);
}
