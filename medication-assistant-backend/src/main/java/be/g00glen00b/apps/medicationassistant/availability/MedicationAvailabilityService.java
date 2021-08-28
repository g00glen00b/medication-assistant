package be.g00glen00b.apps.medicationassistant.availability;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicationAvailabilityService {
    private final MedicationAvailabilityRepository repository;

    public Page<MedicationAvailabilityDTO> findAllByUserId(UUID userId, Pageable pageable) {
        return repository
            .findAllByUserId(userId, pageable)
            .map(MedicationAvailabilityDTO::new);
    }

    @Transactional
    public MedicationAvailabilityDTO updateAvailability(UUID userId, UUID medicationId, MedicationAvailabilityInputDTO input) {
        MedicationAvailability availability = findOrCreate(userId, medicationId, input.getQuantityTypeId());
        availability.setQuantity(input.getQuantity());
        availability.setQuantityTypeId(input.getQuantityTypeId());
        return new MedicationAvailabilityDTO(availability);
    }

    private MedicationAvailability findOrCreate(UUID userId, UUID medicationId, UUID quantityTypeId) {
        MedicationAvailabilityId id = new MedicationAvailabilityId(medicationId, userId);
        return repository
            .findById(id)
            .orElseGet(() -> repository.save(new MedicationAvailability(id, quantityTypeId)));
    }
}
