package be.g00glen00b.apps.medicationassistant.availability;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class CreateMedicationAvailabilityRequestDTO {
    @NotNull(message = "{availability.quantity.notNull}")
    @PositiveOrZero(message = "{availability.quantity.positive}")
    BigDecimal quantity;
    @NotNull(message = "{availability.quantityType.notNull}")
    UUID quantityTypeId;
    @NotNull(message = "{availability.medication.notNull}")
    String medicationName;
    LocalDate expiryDate;

    @JsonCreator
    public CreateMedicationAvailabilityRequestDTO(
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("quantityTypeId") UUID quantityTypeId,
        @JsonProperty("medicationName") String medicationName,
        @JsonProperty("expiryDate") LocalDate expiryDate) {
        this.quantity = quantity;
        this.quantityTypeId = quantityTypeId;
        this.medicationName = medicationName;
        this.expiryDate = expiryDate;
    }
}
