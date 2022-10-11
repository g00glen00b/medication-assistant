package be.g00glen00b.apps.mediminder.medication;

import be.g00glen00b.apps.mediminder.medication.implementation.MedicationEntity;

import java.util.UUID;

public record MedicationDTO(UUID id, String name, MedicationQuantityTypeDTO quantityType) {
    public static MedicationDTO ofEntity(MedicationEntity entity) {
        MedicationQuantityTypeDTO quantityType = MedicationQuantityTypeDTO.ofEntity(entity.getQuantityType());
        return new MedicationDTO(entity.getId(), entity.getName(), quantityType);
    }

    public static MedicationDTO ofId(UUID id) {
        return new MedicationDTO(id, "n/a", null);
    }
}
