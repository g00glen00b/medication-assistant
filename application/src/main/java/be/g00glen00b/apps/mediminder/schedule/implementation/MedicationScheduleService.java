package be.g00glen00b.apps.mediminder.schedule.implementation;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.schedule.*;
import be.g00glen00b.apps.mediminder.user.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Validated
@Service
@RequiredArgsConstructor
class MedicationScheduleService implements MedicationScheduleFacade {
    public static final long MAXIMUM_SCHEDULES_PER_USER = 100;
    public static final PageRequest ALL_SCHEDULES = PageRequest.of(0, 100);
    private final MedicationScheduleEntityRepository repository;
    private final MedicationFacade medicationFacade;
    private final UserFacade userFacade;

    @Override
    public Page<MedicationScheduleDTO> findAll(UUID userId, Pageable pageable) {
        return repository
            .findAllByUserId(userId, pageable)
            .map(this::mapToDTO);
    }

    @Override
    public MedicationScheduleDTO findById(UUID userId, UUID id) {
        return repository
            .findByIdAndUserId(id, userId)
            .map(this::mapToDTO)
            .orElseThrow(() -> new MedicationScheduleNotFoundException(id));
    }

    @Override
    @Transactional
    public MedicationScheduleDTO create(UUID userId, @Valid CreateMedicationScheduleRequestDTO request) {
        validateMedicationId(request.medicationId());
        validateUserId(userId);
        validateScheduleCount(userId);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofEndInclusive(request.startingAt(), request.endingAtInclusive());
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(request.medicationId(), userId, request.quantity(), period, request.interval(), request.time(), request.description());
        MedicationScheduleEntity savedEntity = repository.save(entity);
        return mapToDTO(savedEntity);
    }

    @Override
    @Transactional
    public MedicationScheduleDTO update(UUID userId, UUID id, @Valid UpdateMedicationScheduleRequestDTO request) {
        MedicationScheduleEntity entity = findByIdOrThrowIfNotFound(userId, id);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofEndInclusive(request.startingAt(), request.endingAtInclusive());
        entity.setPeriod(period);
        entity.setQuantity(request.quantity());
        entity.setInterval(request.interval());
        entity.setTime(request.time());
        entity.setDescription(request.description());
        return mapToDTO(entity);
    }

    @Override
    @Transactional
    public void delete(UUID userId, UUID id) {
        MedicationScheduleEntity entity = findByIdOrThrowIfNotFound(userId, id);
        repository.delete(entity);
    }

    @Override
    public Collection<MedicationEventDTO> findEventsByDate(UUID userId, LocalDate date) {
        return repository
            .findAllByUserIdAndDate(userId, date, ALL_SCHEDULES)
            .stream()
            .filter(schedule -> schedule.isActiveOnDate(date))
            .map(entity -> mapToEventDTO(entity, date))
            .toList();
    }

    @Override
    @Transactional
    public MedicationEventDTO completeEvent(UUID userId, UUID id, LocalDate eventDate) {
        MedicationScheduleEntity entity = findByIdOrThrowIfNotFound(userId, id);
        validateEventOccuringOnDate(entity, eventDate);
        validateNoEventCompletedOnDate(entity, eventDate);
        LocalDateTime today = userFacade.calculateTodayForUser(userId);
        MedicationScheduleCompletedEventEntity eventEntity = entity.addCompletedEvent(eventDate, today);
        repository.save(entity);
        MedicationDTO medication = medicationFacade.findByIdOrDummy(entity.getMedicationId());
        return MedicationEventDTO.ofEntity(entity, eventEntity, eventDate, medication);
    }

    private MedicationScheduleEntity findByIdOrThrowIfNotFound(UUID userId, UUID id) {
        return repository.findByIdAndUserId(id, userId).orElseThrow(() -> new MedicationScheduleNotFoundException(id));
    }

    private MedicationScheduleDTO mapToDTO(MedicationScheduleEntity entity) {
        MedicationDTO medication = medicationFacade.findByIdOrDummy(entity.getMedicationId());
        return MedicationScheduleDTO.ofEntity(entity, medication);
    }

    private MedicationEventDTO mapToEventDTO(MedicationScheduleEntity entity, LocalDate eventDate) {
        MedicationDTO medication = medicationFacade.findByIdOrDummy(entity.getMedicationId());
        LocalDateTime eventDateTime = LocalDateTime.of(eventDate, entity.getTime());
        return repository
            .findCompletedEventByIdAndEventDate(entity.getId(), eventDateTime)
            .map(eventEntity -> MedicationEventDTO.ofEntity(entity, eventEntity, eventDate, medication))
            .orElseGet(() -> MedicationEventDTO.ofEntity(entity, eventDate, medication));
    }

    private void validateUserId(UUID userId) {
        if (!userFacade.existsById(userId)) {
            throw new InvalidMedicationScheduleException("User is not valid");
        }
    }

    private void validateMedicationId(UUID medicationId) {
        if (!medicationFacade.existsById(medicationId)) {
            throw new InvalidMedicationScheduleException("Medication is not valid");
        }
    }

    private void validateEventOccuringOnDate(MedicationScheduleEntity entity, LocalDate date) {
        if (!entity.isActiveOnDate(date)) {
            throw new MedicationEventNotFoundException(date);
        }
    }

    private void validateNoEventCompletedOnDate(MedicationScheduleEntity entity, LocalDate date) {
        LocalDateTime eventDateTime = LocalDateTime.of(date, entity.getTime());
        if (repository.findCompletedEventByIdAndEventDate(entity.getId(), eventDateTime).isPresent()) {
            throw new InvalidMedicationEventException("This medication was already taken at this time");
        }
    }

    private void validateScheduleCount(UUID userId) {
        if (repository.countAllByUserId(userId) > MAXIMUM_SCHEDULES_PER_USER) {
            throw new InvalidMedicationScheduleException("We only allow 100 schedules per user. Please delete an existing schedule before adding a new one.");
        }
    }
}
