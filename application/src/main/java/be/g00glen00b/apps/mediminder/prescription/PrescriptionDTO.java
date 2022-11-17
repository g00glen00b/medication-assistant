package be.g00glen00b.apps.mediminder.prescription;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;

import java.math.BigDecimal;

public record PrescriptionDTO(
    MedicationDTO medication,
    BigDecimal requiredQuantity,
    BigDecimal availableQuantity,
    BigDecimal neededQuantity,
    BigDecimal initialQuantityPerItem,
    BigDecimal neededPrescriptions) {
}
