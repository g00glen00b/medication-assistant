package be.g00glen00b.apps.medicationassistant.schedule;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class MedicationScheduleEventService implements MedicationScheduleEventFacade {
    public static final Sort SORT_BY_TIME_DESCENDING = Sort.by(Sort.Direction.DESC, "time");
    public static final PageRequest FIRST_RECORD_SORTED_BY_TIME_DESCENDING = PageRequest.of(0, 1, SORT_BY_TIME_DESCENDING);
    private final MedicationScheduleEventRepository repository;
    private final MedicationScheduleRepository scheduleRepository;
    private final MedicationClient medicationClient;
    private final List<MedicationScheduleEventCompleteListener> completeListeners;

    @Override
    @Transactional
    public MedicationScheduleEventDTO createNextEvent(UUID scheduleId, UUID userId) {
        return scheduleRepository
            .findByIdAndUserId(scheduleId, userId)
            .map(this::createNextEvent)
            .map(repository::save)
            .map(this::mapTODTO)
            .orElseThrow(LastMedicationScheduleEventException::new);
    }

    @Override
    public Page<MedicationScheduleEventDTO> findAllActiveByUserId(UUID userId, Pageable pageable) {
        return repository
            .findAllActiveByUserId(userId, pageable)
            .map(this::mapTODTO);
    }

    @Override
    @Transactional
    public void deleteBySchedule(UUID scheduleId, UUID userId) {
        repository.deleteAllByScheduleIdAndUserId(scheduleId, userId);
    }

    @Transactional
    public MedicationScheduleEvent createNextEvent(MedicationSchedule schedule) {
        Optional<MedicationScheduleEvent> lastEvent = findLastEvent(schedule);
        lastEvent.ifPresent(this::deactivateCurrentEvent);
        return lastEvent
            .flatMap(MedicationScheduleEvent::next)
            .orElseGet(() -> new MedicationScheduleEvent(schedule));
    }

    @Transactional
    public void deactivateCurrentEvent(MedicationScheduleEvent event) {
        event.deactivate();
        MedicationScheduleEventDTO eventDTO = mapTODTO(event);
        MedicationScheduleCompletedEventDTO completedEventDTO = new MedicationScheduleCompletedEventDTO(event.getSchedule().getUserId(), eventDTO);
        completeListeners.forEach(listener -> listener.listen(completedEventDTO));
    }

    public Optional<MedicationScheduleEvent> findLastEvent(MedicationSchedule schedule) {
        return repository
            .findAllByScheduleId(schedule.getId(), FIRST_RECORD_SORTED_BY_TIME_DESCENDING)
            .stream()
            .findAny();
    }

    private MedicationScheduleEventDTO mapTODTO(MedicationScheduleEvent event) {
        MedicationDTO medication = medicationClient
            .findMedicationById(event.getSchedule().getMedicationId())
            .orElse(null);
        MedicationQuantityTypeDTO quantityType = medicationClient
            .findQuantityTypeById(event.getSchedule().getQuantityTypeId())
            .orElse(null);
        return new MedicationScheduleEventDTO(event, medication, quantityType);
    }
}
