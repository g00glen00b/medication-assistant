package be.g00glen00b.apps.mediminder.medication;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MedicationNotFoundException extends RuntimeException {
    private final UUID medicationId;

    @Override
    public String getMessage() {
        return "Medication with ID '" + medicationId + "' was not found";
    }
}
