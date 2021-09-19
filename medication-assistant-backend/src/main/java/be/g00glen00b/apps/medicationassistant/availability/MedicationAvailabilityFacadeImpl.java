package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.UUID;

@Service
@Validated
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
    public MedicationAvailabilityDTO create(UUID userId, @Valid CreateMedicationAvailabilityRequestDTO input) {
        MedicationAvailability availability = repository.save(input.mapToEntity(userId));
        return mapToDTO(availability);
    }

    @Override
    @Transactional
    public MedicationAvailabilityDTO updateQuantity(UUID userId, UUID id, @Valid UpdateMedicationAvailabilityQuantityInputDTO input) {
        MedicationAvailability availability = findEntityOrThrowException(userId, id);
        availability.setQuantity(input.getQuantity());
        return mapToDTO(availability);
    }

    @Override
    @Transactional
    public MedicationAvailabilityDTO increaseQuantity(UUID userId, UUID id) {
        MedicationAvailability availability = findEntityOrThrowException(userId, id);
        availability.increaseQuantityByOne();
        return mapToDTO(availability);
    }

    @Override
    @Transactional
    public MedicationAvailabilityDTO decreaseQuantity(UUID userId, UUID id) {
        MedicationAvailability availability = findEntityOrThrowException(userId, id);
        availability.decreaseQuantityByOne();
        return mapToDTO(availability);
    }

    @Override
    @Transactional
    public void delete(UUID userId, UUID id) {
        MedicationAvailability availability = findEntityOrThrowException(userId, id);
        repository.delete(availability);
    }

    private MedicationAvailability findEntityOrThrowException(UUID userId, UUID id) {
        return repository
            .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new MedicationAvailabilityNotFoundException("You have no medication quantity with that ID"));
    }

    private MedicationAvailabilityDTO mapToDTO(MedicationAvailability availability) {
        MedicationDTO medication = medicationClient
            .findMedicationById(availability.getMedicationId())
            .orElse(null);
        MedicationQuantityTypeDTO quantityType = medicationClient
            .findQuantityTypeById(availability.getQuantityTypeId())
            .orElse(null);
        return new MedicationAvailabilityDTO(quantityType, medication, availability);
    }
}
