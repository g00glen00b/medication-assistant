package be.g00glen00b.apps.mediminder.availability;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MedicationAvailabilityNotFoundException extends RuntimeException {
    private final UUID id;

    @Override
    public String getMessage() {
        return "Medication availability with ID '" + id + "' was not found";
    }
}
