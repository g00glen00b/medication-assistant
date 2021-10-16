package be.g00glen00b.apps.medicationassistant.core;

import java.time.LocalDate;

public interface LocalDatePeriod {
    LocalDate getStartingAt();
    LocalDate getEndingAt();
}
