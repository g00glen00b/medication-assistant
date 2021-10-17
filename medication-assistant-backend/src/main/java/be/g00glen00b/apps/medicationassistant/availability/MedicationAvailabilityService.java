package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.core.UnsupportedBinaryOperator;
import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
class MedicationAvailabilityService implements MedicationAvailabilityFacade {
    private static final Sort SORT_BY_EXPIRY_DATE = Sort.by(Sort.Direction.ASC, "expiryDate");
    private static final Pageable FIRST_THOUSAND_RECORDS_SORTED_BY_EXPIRY_DATE = PageRequest.of(0, 1000, SORT_BY_EXPIRY_DATE);
    private final MedicationAvailabilityRepository repository;
    private final MedicationClient medicationClient;
    private final Clock clock;

    @Override
    public Page<MedicationAvailabilityDTO> findAllByUserId(UUID userId, Pageable pageable) {
        return repository
            .findAllByUserId(userId, pageable)
            .map(this::mapToDTO);
    }

    @Override
    @Transactional
    public MedicationAvailabilityDTO create(UUID userId, @Valid CreateMedicationAvailabilityRequestDTO input) {
        MedicationDTO medication = medicationClient.findOrCreateMedication(input.getMedicationName());
        MedicationAvailability availability = repository.save(new MedicationAvailability(
            userId,
            medication.getId(),
            input.getQuantityTypeId(),
            input.getQuantity(),
            input.getQuantity(),
            input.getExpiryDate()));
        return mapToDTO(availability);
    }

    @Override
    @Transactional
    public MedicationAvailabilityDTO update(UUID userId, UUID id, @Valid UpdateMedicationAvailabilityInputDTO input) {
        MedicationAvailability availability = findEntityOrThrowException(userId, id);
        availability.setInitialQuantity(input.getInitialQuantity());
        availability.setQuantity(input.getQuantity());
        availability.setQuantityTypeId(input.getQuantityTypeId());
        availability.setExpiryDate(input.getExpiryDate());
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
    public void decreaseOverallQuantity(UUID userId, UUID medicationId, UUID quantityTypeId, BigDecimal quantity) {
        LocalDate today = LocalDate.now(clock);
        BigDecimal remainingQuantity = repository
            .findAllNonEmptyNonExpiredByUserAndMedication(userId, medicationId, quantityTypeId, today, FIRST_THOUSAND_RECORDS_SORTED_BY_EXPIRY_DATE)
            .stream()
            .reduce(quantity, this::decreaseQuantity, new UnsupportedBinaryOperator<>());
        if (!isZero(remainingQuantity)) {
            // TODO: Alert user that there's no more medication available!
        }
    }

    @Transactional
    public BigDecimal decreaseQuantity(BigDecimal remainingQuantity, MedicationAvailability availability) {
        if (isZero(remainingQuantity)) {
            return remainingQuantity;
        } else if (availability.isContainingMoreOrEqualThan(remainingQuantity)) {
            availability.decreaseQuantity(remainingQuantity);
            return BigDecimal.ZERO;
        } else {
            BigDecimal newRemainingQuantity = remainingQuantity.subtract(availability.getQuantity());
            availability.setQuantity(BigDecimal.ZERO);
            return newRemainingQuantity;
        }
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

    private boolean isZero(BigDecimal remainingQuantity) {
        return BigDecimal.ZERO.compareTo(remainingQuantity) == 0;
    }
}
