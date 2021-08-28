package be.g00glen00b.apps.medicationassistant.availability;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class MedicationAvailabilityInputDTO {
    BigDecimal quantity;
    UUID quantityTypeId;

    @JsonCreator
    public MedicationAvailabilityInputDTO(
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("quantityTypeId") UUID quantityTypeId) {
        this.quantity = quantity;
        this.quantityTypeId = quantityTypeId;
    }
}
