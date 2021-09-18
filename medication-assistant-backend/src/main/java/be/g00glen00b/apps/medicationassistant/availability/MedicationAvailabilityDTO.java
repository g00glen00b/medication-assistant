package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class MedicationAvailabilityDTO {
    MedicationQuantityTypeDTO quantityType;
    MedicationDTO medication;
    BigDecimal quantity;

    public MedicationAvailabilityDTO(MedicationQuantityTypeDTO quantityType, MedicationDTO medication, MedicationAvailability availability) {
        this.quantityType = quantityType;
        this.medication = medication;
        this.quantity = availability.getQuantity();
    }
}
