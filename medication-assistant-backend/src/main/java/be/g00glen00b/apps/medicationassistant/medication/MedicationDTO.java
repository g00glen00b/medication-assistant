package be.g00glen00b.apps.medicationassistant.medication;

import lombok.Value;

import java.util.UUID;

@Value
public class MedicationDTO {
    UUID id;
    String name;

    public MedicationDTO(Medication medication) {
        this.id = medication.getId();
        this.name = medication.getName();
    }

    public MedicationDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
