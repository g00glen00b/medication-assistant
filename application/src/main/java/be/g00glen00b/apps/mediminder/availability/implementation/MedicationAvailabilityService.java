package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.availability.*;
import be.g00glen00b.apps.mediminder.medication.CreateMedicationRequestDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.user.UserFacade;
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
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.BinaryOperator;

@Service
@Validated
@RequiredArgsConstructor
class MedicationAvailabilityService implements MedicationAvailabilityFacade {
    private final MedicationAvailabilityEntityRepository repository;
    private final MedicationFacade medicationFacade;
    private final UserFacade userFacade;

    @Override
    public Page<MedicationAvailabilityDTO> findAllNonEmptyNonExpiredByUserId(UUID userId, Pageable pageable) {
        LocalDate today = userFacade.calculateTodayForUser(userId).toLocalDate();
        return repository
            .findAllNonEmptyNonExpiredByUserId(userId, today, pageable)
            .map(this::mapToDTO);
    }


    @Override
    @Transactional
    public MedicationAvailabilityDTO create(UUID userId, @Valid CreateMedicationAvailabilityRequestDTO request) {
        validateUserId(userId);
        validateQuantityLessThanInitialQuantity(request.quantity(), request.initialQuantity());
        MedicationDTO medication = createMedication(request);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medication.id(), userId, request.quantity(), request.initialQuantity(), request.expiryDate());
        MedicationAvailabilityEntity savedEntity = repository.save(entity);
        return MedicationAvailabilityDTO.ofEntity(savedEntity, medication);
    }

    @Override
    @Transactional
    public MedicationAvailabilityDTO update(UUID userId, UUID id, @Valid UpdateMedicationAvailabilityRequestDTO request) {
        validateUserId(userId);
        validateQuantityLessThanInitialQuantity(request.quantity(), request.initialQuantity());
        MedicationAvailabilityEntity entity = findByIdAndUserIdOrThrowException(userId, id);
        entity.setQuantity(request.quantity());
        entity.setInitialQuantity(request.initialQuantity() == null ? request.quantity() : request.initialQuantity());
        entity.setExpiryDate(request.expiryDate());
        return mapToDTO(entity);
    }

    @Override
    @Transactional
    public void delete(UUID userId, UUID id) {
        MedicationAvailabilityEntity entity = findByIdAndUserIdOrThrowException(userId, id);
        repository.delete(entity);
    }

    @Override
    public MedicationAvailabilityDTO findById(UUID userId, UUID id) {
        return repository
            .findByIdAndUserId(id, userId)
            .map(this::mapToDTO)
            .orElseThrow(() -> new MedicationAvailabilityNotFoundException(id));
    }

    @Override
    @Transactional
    public BigDecimal removeQuantity(UUID userId, UUID medicationId, BigDecimal quantity) {
        LocalDate today = userFacade.calculateTodayForUser(userId).toLocalDate();
        PageRequest resultsByExpiryDate = PageRequest.of(0, 100, Sort.by("expiryDate"));
        return repository
            .findAllNonEmptyNonExpiredByUserIdAndMedicationId(userId, medicationId, today, resultsByExpiryDate)
            .getContent()
            .stream()
            .reduce(quantity, (q, entity) -> entity.subtractQuantity(q), unsupportedOperator());
    }

    private MedicationAvailabilityEntity findByIdAndUserIdOrThrowException(UUID userId, UUID id) {
        return repository
            .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new MedicationAvailabilityNotFoundException(id));
    }

    private void validateUserId(UUID userId) {
        if (!userFacade.existsById(userId)) {
            throw new InvalidMedicationAvailabilityException("User is not valid");
        }
    }

    private void validateQuantityLessThanInitialQuantity(BigDecimal quantity, BigDecimal initialQuanitty) {
        if (initialQuanitty != null && quantity.compareTo(initialQuanitty) > 0) {
            throw new InvalidMedicationAvailabilityException("The quantity of the medication exceeds the initial quantity");
        }
    }

    private MedicationDTO createMedication(CreateMedicationAvailabilityRequestDTO request) {
        return medicationFacade.findOrCreate(new CreateMedicationRequestDTO(request.medicationName(), request.quantityTypeId()));
    }

    private MedicationAvailabilityDTO mapToDTO(MedicationAvailabilityEntity entity) {
        MedicationDTO medication = medicationFacade.findByIdOrDummy(entity.getMedicationId());
        return MedicationAvailabilityDTO.ofEntity(entity, medication);
    }

    private static <T> BinaryOperator<T> unsupportedOperator() {
        return (arg1, arg2) -> {throw new UnsupportedOperationException();};
    }
}
