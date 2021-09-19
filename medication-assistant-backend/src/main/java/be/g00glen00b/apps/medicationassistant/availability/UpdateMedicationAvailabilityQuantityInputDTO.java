package be.g00glen00b.apps.medicationassistant.availability;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Value
public class UpdateMedicationAvailabilityQuantityInputDTO {
    @NotNull(message = "{availability.quantity.notNull}")
    @PositiveOrZero(message = "{availability.quantity.positive}")
    BigDecimal quantity;

    @JsonCreator
    public UpdateMedicationAvailabilityQuantityInputDTO(@JsonProperty("quantity") BigDecimal quantity) {
        this.quantity = quantity;
    }
}
