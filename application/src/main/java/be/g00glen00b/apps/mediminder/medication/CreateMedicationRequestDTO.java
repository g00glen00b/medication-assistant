package be.g00glen00b.apps.mediminder.medication;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateMedicationRequestDTO(
    @NotNull(message = "{medication.name.notNull}")
    @NotEmpty(message = "{medication.name.notNull}")
    @Size(max = 256, message = "{medication.name.size}")
    String name,
    @NotNull(message = "{medication.quantityType.notNull}")
    UUID quantityTypeId) {
}
