package be.g00glen00b.apps.medicationassistant.availability;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class MedicationAvailabilityDTO {
    UUID medicationId;
    BigDecimal quantity;
    UUID quantityTypeId;

    public MedicationAvailabilityDTO(MedicationAvailability availability) {
        this.medicationId = availability.getId().getMedicationId();
        this.quantity = availability.getQuantity();
        this.quantityTypeId = availability.getQuantityTypeId();
    }
}
