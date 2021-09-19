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
    @NotNull(message = "{availability.initialQuantity.notNull}")
    @PositiveOrZero(message = "{availability.initialQuantity.positive}")
    BigDecimal initialQuantity;
    @NotNull(message = "{availability.quantityType.notNull}")
    UUID quantityTypeId;
    @NotNull(message = "{availability.medication.notNull}")
    UUID medicationId;
    LocalDate expiryDate;

    @JsonCreator
    public CreateMedicationAvailabilityRequestDTO(
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("initialQuantity") BigDecimal initialQuantity,
        @JsonProperty("quantityTypeId") UUID quantityTypeId,
        @JsonProperty("medicationId") UUID medicationId,
        @JsonProperty("expiryDate") LocalDate expiryDate) {
        this.quantity = quantity;
        this.initialQuantity = initialQuantity;
        this.quantityTypeId = quantityTypeId;
        this.medicationId = medicationId;
        this.expiryDate = expiryDate;
    }

    public MedicationAvailability mapToEntity(UUID userId) {
        return new MedicationAvailability(
            userId,
            this.medicationId,
            this.quantityTypeId,
            this.quantity,
            this.initialQuantity,
            this.expiryDate
        );
    }
}
