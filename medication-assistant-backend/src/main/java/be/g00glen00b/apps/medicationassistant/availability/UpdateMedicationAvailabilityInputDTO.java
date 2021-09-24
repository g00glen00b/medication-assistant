package be.g00glen00b.apps.medicationassistant.availability;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class UpdateMedicationAvailabilityInputDTO {
    @NotNull(message = "{availability.quantity.notNull}")
    @PositiveOrZero(message = "{availability.quantity.positive}")
    BigDecimal quantity;
    @NotNull(message = "{availability.quantityType.notNull}")
    UUID quantityTypeId;
    @NotNull(message = "{availability.initialQuantity.notNull}")
    @PositiveOrZero(message = "{availability.initialQuantity.positive}")
    BigDecimal initialQuantity;
    LocalDate expiryDate;

    @JsonCreator
    public UpdateMedicationAvailabilityInputDTO(
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("quantityTypeId") UUID quantityTypeId,
        @JsonProperty("initialQuantity") BigDecimal initialQuantity,
        @JsonProperty("expiryDate") LocalDate expiryDate) {
        this.quantity = quantity;
        this.quantityTypeId = quantityTypeId;
        this.initialQuantity = initialQuantity;
        this.expiryDate = expiryDate;
    }
}
