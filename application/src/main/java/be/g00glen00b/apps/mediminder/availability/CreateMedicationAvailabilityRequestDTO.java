package be.g00glen00b.apps.mediminder.availability;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateMedicationAvailabilityRequestDTO(
    @NotNull(message = "{availability.medication.notNull}")
    @Size(max = 256, message = "{availability.medication.size}")
    String medicationName,
    @NotNull(message = "{availability.quantityType.notNull}")
    UUID quantityTypeId,
    @NotNull(message = "{availability.quantity.notNull}")
    @PositiveOrZero(message = "{availability.quantity.positive}")
    BigDecimal quantity,
    @PositiveOrZero(message = "{availability.initialQuantity.positive}")
    BigDecimal initialQuantity,
    @FutureOrPresent(message = "{availability.expiryDate.future}")
    LocalDate expiryDate) {
}
