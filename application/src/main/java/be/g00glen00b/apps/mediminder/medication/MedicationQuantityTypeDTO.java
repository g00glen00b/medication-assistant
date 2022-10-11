package be.g00glen00b.apps.mediminder.medication;

import be.g00glen00b.apps.mediminder.medication.implementation.MedicationQuantityTypeEntity;

import java.util.UUID;

public record MedicationQuantityTypeDTO(UUID id, String name) {
    public static MedicationQuantityTypeDTO ofEntity(MedicationQuantityTypeEntity entity) {
        return new MedicationQuantityTypeDTO(entity.getId(), entity.getName());
    }
}
