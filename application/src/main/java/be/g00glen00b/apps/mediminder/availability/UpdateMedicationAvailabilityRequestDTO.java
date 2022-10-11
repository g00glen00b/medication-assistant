package be.g00glen00b.apps.mediminder.availability;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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
