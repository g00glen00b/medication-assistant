package be.g00glen00b.apps.medicationassistant.schedule;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class MedicationScheduleService implements MedicationScheduleFacade {
    private final MedicationScheduleRepository repository;
    private final MedicationClient medicationClient;

    @Override
    public Page<MedicationScheduleDTO> findAllByUserId(UUID userId, Pageable pageable) {
        return repository
            .findAllByUserId(userId, pageable)
            .map(this::mapToDTO);
    }

    @Override
    @Transactional
    public MedicationScheduleDTO create(UUID userId, @Valid CreateMedicationScheduleRequestDTO input) {
        MedicationDTO medication = medicationClient.findOrCreateMedication(input.getMedicationName());
        MedicationSchedule schedule = repository.save(new MedicationSchedule(
            userId,
            medication.getId(),
            input.getQuantityTypeId(),
            input.getQuantity(),
            input.getStartingAt(),
            input.getInterval(),
            input.getTime()));
        return mapToDTO(schedule);
    }

    @Override
    @Transactional
    public void delete(UUID userId, UUID id) {
        MedicationSchedule schedule = findEntityOrThrowException(userId, id);
        repository.delete(schedule);
    }

    private MedicationSchedule findEntityOrThrowException(UUID userId, UUID id) {
        return repository
            .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new MedicationScheduleNotFoundException("You have no medication quantity with that ID"));
    }

    private MedicationScheduleDTO mapToDTO(MedicationSchedule schedule) {
        MedicationDTO medication = medicationClient
            .findMedicationById(schedule.getMedicationId())
            .orElse(null);
        MedicationQuantityTypeDTO quantityType = medicationClient
            .findQuantityTypeById(schedule.getQuantityTypeId())
            .orElse(null);
        return new MedicationScheduleDTO(quantityType, medication, schedule);
    }
}
