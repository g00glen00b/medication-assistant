package be.g00glen00b.apps.mediminder.availability;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;

import java.math.BigDecimal;

public record MedicationAvailabilityTotalDTO(MedicationDTO medication, BigDecimal initialQuantityPerItem, BigDecimal totalQuantity) {
}
