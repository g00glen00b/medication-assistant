package be.g00glen00b.apps.mediminder.schedule;

import be.g00glen00b.apps.mediminder.schedule.implementation.MedicationScheduleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MedicationTakenEvent(UUID userId, UUID scheduleId, UUID medicationId, BigDecimal quantity, LocalDateTime eventDate, LocalDateTime completedDate) {
    public static MedicationTakenEvent ofEntity(MedicationScheduleEntity entity, LocalDate eventDate, LocalDateTime completedDate) {
        return new MedicationTakenEvent(entity.getUserId(), entity.getId(), entity.getMedicationId(), entity.getQuantity(), LocalDateTime.of(eventDate, entity.getTime()), completedDate);
    }
}
