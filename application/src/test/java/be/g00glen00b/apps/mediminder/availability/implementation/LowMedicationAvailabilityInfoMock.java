package be.g00glen00b.apps.mediminder.availability.implementation;

import java.time.Instant;
import java.util.UUID;

record LowMedicationAvailabilityInfoMock(UUID medicationId, UUID userId, Instant createdDate) implements LowMedicationAvailabilityInfo {

    @Override
    public UUID getMedicationId() {
        return this.medicationId;
    }

    @Override
    public UUID getUserId() {
        return this.userId;
    }

    @Override
    public Instant getCreatedDate() {
        return this.createdDate;
    }
}
