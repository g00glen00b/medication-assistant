package be.g00glen00b.apps.mediminder.availability.implementation;

import java.time.Instant;
import java.util.UUID;

public interface LowMedicationAvailabilityInfo {
    UUID getMedicationId();
    UUID getUserId();
    Instant getCreatedDate();
}
