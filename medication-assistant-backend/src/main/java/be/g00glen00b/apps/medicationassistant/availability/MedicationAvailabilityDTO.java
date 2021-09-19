package be.g00glen00b.apps.medicationassistant.availability;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class MedicationAvailabilityDTO {
    UUID id;
    MedicationQuantityTypeDTO quantityType;
    MedicationDTO medication;
    BigDecimal quantity;
    BigDecimal initialQuantity;
    LocalDate expiryDate;

    public MedicationAvailabilityDTO(MedicationQuantityTypeDTO quantityType, MedicationDTO medication, MedicationAvailability availability) {
        this.quantityType = quantityType;
        this.medication = medication;
        this.id = availability.getId();
        this.quantity = availability.getQuantity();
        this.initialQuantity = availability.getInitialQuantity();
        this.expiryDate = availability.getExpiryDate();
    }
}
