package be.g00glen00b.apps.mediminder.availability;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateMedicationAvailabilityRequestDTO(
    @NotNull(message = "{availability.quantity.notNull}")
    @PositiveOrZero(message = "{availability.quantity.positive}")
    BigDecimal quantity,
    @PositiveOrZero(message = "{availability.initialQuantity.positive}")
    BigDecimal initialQuantity,
    @FutureOrPresent(message = "{availability.expiryDate.future}")
    LocalDate expiryDate) {
}
