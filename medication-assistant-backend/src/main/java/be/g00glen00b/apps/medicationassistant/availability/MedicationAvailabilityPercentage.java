package be.g00glen00b.apps.medicationassistant.availability;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationAvailabilityPercentage {
    private UUID medicationId;
    private UUID userId;
    private BigDecimal percentage;
}
