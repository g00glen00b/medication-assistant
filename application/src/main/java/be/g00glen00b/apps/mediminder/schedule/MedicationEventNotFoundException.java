package be.g00glen00b.apps.mediminder.schedule;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class MedicationEventNotFoundException extends RuntimeException {
    private final LocalDate date;

    @Override
    public String getMessage() {
        return "This medication should not be taken at " + DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    }
}
