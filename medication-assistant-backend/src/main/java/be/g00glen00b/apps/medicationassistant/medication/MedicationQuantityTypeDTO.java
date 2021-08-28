package be.g00glen00b.apps.medicationassistant.medication;

import lombok.Value;

import java.util.UUID;

@Value
public class MedicationQuantityTypeDTO {
    UUID id;
    String name;

    public MedicationQuantityTypeDTO(MedicationQuantityType quantity) {
        this.id = quantity.getId();
        this.name = quantity.getName();
    }
}
