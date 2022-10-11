package be.g00glen00b.apps.mediminder.schedule;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.schedule.implementation.MedicationScheduleCompletedEventEntity;
import be.g00glen00b.apps.mediminder.schedule.implementation.MedicationScheduleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MedicationEventDTO(
    UUID scheduleId,
    MedicationDTO medication,
    BigDecimal quantity,
    LocalDateTime eventDate,
    String description,
    LocalDateTime completedDate) {

    public static MedicationEventDTO ofEntity(MedicationScheduleEntity entity, LocalDate eventDate, MedicationDTO medication) {
        LocalDateTime eventDateTime = LocalDateTime.of(eventDate, entity.getTime());
        return new MedicationEventDTO(entity.getId(), medication, entity.getQuantity(), eventDateTime, entity.getDescription(), null);
    }

    public static MedicationEventDTO ofEntity(MedicationScheduleEntity entity, MedicationScheduleCompletedEventEntity eventEntity, LocalDate eventDate, MedicationDTO medication) {
        LocalDateTime eventDateTime = LocalDateTime.of(eventDate, entity.getTime());
        return new MedicationEventDTO(entity.getId(), medication, entity.getQuantity(), eventDateTime, entity.getDescription(), eventEntity.getCompletedDate());
    }
}
