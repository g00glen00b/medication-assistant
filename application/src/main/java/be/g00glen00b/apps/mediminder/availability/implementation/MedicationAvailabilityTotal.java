package be.g00glen00b.apps.mediminder.availability.implementation;

import java.math.BigDecimal;
import java.util.UUID;

public interface MedicationAvailabilityTotal {
    UUID getMedicationId();
    BigDecimal getInitialQuantityPerItem();
    BigDecimal getTotalQuantity();
}
