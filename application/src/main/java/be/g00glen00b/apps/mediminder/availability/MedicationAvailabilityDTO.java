package be.g00glen00b.apps.mediminder.availability;

import be.g00glen00b.apps.mediminder.availability.implementation.MedicationAvailabilityEntity;
import be.g00glen00b.apps.mediminder.medication.MedicationDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MedicationAvailabilityDTO(UUID id, MedicationDTO medication, BigDecimal quantity, BigDecimal initialQuantity, LocalDate expiryDate) {
    public static MedicationAvailabilityDTO ofEntity(MedicationAvailabilityEntity entity, MedicationDTO medication) {
        return new MedicationAvailabilityDTO(entity.getId(), medication, entity.getQuantity(), entity.getInitialQuantity(), entity.getExpiryDate());
    }
}
