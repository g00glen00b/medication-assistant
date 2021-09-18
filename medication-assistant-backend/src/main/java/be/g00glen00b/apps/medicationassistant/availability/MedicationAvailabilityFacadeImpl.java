package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicationAvailabilityFacadeImpl implements MedicationAvailabilityFacade {
    private final MedicationAvailabilityRepository repository;
    private final MedicationClient medicationClient;

    @Override
    public Page<MedicationAvailabilityDTO> findAllByUserId(UUID userId, Pageable pageable) {
        return repository
            .findAllByUserId(userId, pageable)
            .map(this::mapToDTO);
    }

    @Override
    @Transactional
    public MedicationAvailabilityDTO updateAvailability(UUID userId, UUID medicationId, MedicationAvailabilityInputDTO input) {
        MedicationAvailability availability = findOrCreate(userId, medicationId, input.getQuantityTypeId());
        availability.setQuantity(input.getQuantity());
        availability.setQuantityTypeId(input.getQuantityTypeId());
        return mapToDTO(availability);
    }

    private MedicationAvailability findOrCreate(UUID userId, UUID medicationId, UUID quantityTypeId) {
        MedicationAvailabilityId id = new MedicationAvailabilityId(medicationId, userId);
        return repository
            .findById(id)
            .orElseGet(() -> repository.save(new MedicationAvailability(id, quantityTypeId)));
    }

    private MedicationAvailabilityDTO mapToDTO(MedicationAvailability availability) {
        MedicationDTO medication = medicationClient
            .findMedicationById(availability.getId().getMedicationId())
            .orElseGet(() -> new MedicationDTO(availability.getId().getMedicationId(), "not found"));
        MedicationQuantityTypeDTO quantityType = medicationClient
            .findQuantityTypeById(availability.getQuantityTypeId())
            .orElseGet(() -> new MedicationQuantityTypeDTO(availability.getQuantityTypeId(), "not found"));
        return new MedicationAvailabilityDTO(quantityType, medication, availability);
    }
}
