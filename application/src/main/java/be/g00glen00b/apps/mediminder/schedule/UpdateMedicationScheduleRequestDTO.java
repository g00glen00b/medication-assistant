package be.g00glen00b.apps.mediminder.schedule;

import be.g00glen00b.apps.mediminder.schedule.implementation.PositivePeriod;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

public record UpdateMedicationScheduleRequestDTO(
    @NotNull(message = "{schedule.quantity.notNull}")
    @Positive(message = "{schedule.quantity.positive}")
    BigDecimal quantity,
    @NotNull(message = "{schedule.startingAt.notNull}")
    @FutureOrPresent(message = "{schedule.startingAt.future}")
    LocalDate startingAt,
    @FutureOrPresent(message = "{schedule.endingAt.future}")
    LocalDate endingAtInclusive,
    @NotNull(message = "{schedule.interval.notNull}")
    @PositivePeriod(message = "{schedule.interval.positive}")
    Period interval,
    @NotNull(message = "{schedule.time.notNull}")
    LocalTime time,
    @Size(max = 256, message = "{schedule.description.size}")
    String description) {
}
