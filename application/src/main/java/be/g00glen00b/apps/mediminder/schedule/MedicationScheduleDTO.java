package be.g00glen00b.apps.mediminder.schedule;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.schedule.implementation.MedicationScheduleEntity;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.Period;
import java.util.UUID;

public record MedicationScheduleDTO(
    UUID id,
    MedicationDTO medication,
    BigDecimal quantity,
    MedicationSchedulePeriodDTO period,
    Period interval,
    LocalTime time,
    String description) {

    public static MedicationScheduleDTO ofEntity(MedicationScheduleEntity entity, MedicationDTO medication) {
        MedicationSchedulePeriodDTO period = MedicationSchedulePeriodDTO.ofEntity(entity.getPeriod());
        return new MedicationScheduleDTO(entity.getId(), medication, entity.getQuantity(), period, entity.getInterval(), entity.getTime(), entity.getDescription());
    }
}
