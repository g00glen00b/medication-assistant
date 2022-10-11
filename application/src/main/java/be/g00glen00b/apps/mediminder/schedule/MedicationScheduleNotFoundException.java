package be.g00glen00b.apps.mediminder.schedule;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MedicationScheduleNotFoundException extends RuntimeException {
    private final UUID id;

    @Override
    public String getMessage() {
        return "Schedule with ID '" + id + "' was not found";
    }
}
