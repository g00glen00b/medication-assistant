package be.g00glen00b.apps.mediminder.schedule;

import be.g00glen00b.apps.mediminder.schedule.implementation.MedicationSchedulePeriod;

import java.time.LocalDate;

public record MedicationSchedulePeriodDTO(LocalDate startingAt, LocalDate endingAtInclusive) {
    public static MedicationSchedulePeriodDTO ofEntity(MedicationSchedulePeriod entity) {
        return new MedicationSchedulePeriodDTO(entity.getStartingAt(), entity.getEndingAtInclusive());
    }
}
