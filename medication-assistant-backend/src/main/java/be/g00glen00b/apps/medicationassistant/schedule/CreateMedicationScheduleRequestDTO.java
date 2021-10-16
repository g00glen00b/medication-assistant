package be.g00glen00b.apps.medicationassistant.schedule;

import be.g00glen00b.apps.medicationassistant.core.LocalDatePeriod;
import be.g00glen00b.apps.medicationassistant.core.MinLocalDatePeriod;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.UUID;

@Value
@MinLocalDatePeriod
public class CreateMedicationScheduleRequestDTO implements LocalDatePeriod {
    @NotNull(message = "{schedule.quantity.notNull}")
    @PositiveOrZero(message = "{schedule.quantity.positive}")
    BigDecimal quantity;
    @NotNull(message = "{schedule.quantityType.notNull}")
    UUID quantityTypeId;
    @NotNull(message = "{schedule.medication.notNull}")
    String medicationName;
    @NotNull(message = "{schedule.startingAt.notNull}")
    @FutureOrPresent(message = "{schedule.startingAt.future}")
    LocalDate startingAt;
    @FutureOrPresent(message = "{schedule.endingAt.future}")
    LocalDate endingAt;
    @NotNull(message = "{schedule.interval.notNull}")
    Period interval;
    @NotNull(message = "{schedule.time.notNull}")
    LocalTime time;

    @JsonCreator
    public CreateMedicationScheduleRequestDTO(
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("quantityTypeId") UUID quantityTypeId,
        @JsonProperty("medicationName") String medicationName,
        @JsonProperty("startingAt") LocalDate startingAt,
        @JsonProperty("endingAt") LocalDate endingAt,
        @JsonProperty("interval") Period interval,
        @JsonProperty("time") LocalTime time) {
        this.quantity = quantity;
        this.quantityTypeId = quantityTypeId;
        this.medicationName = medicationName;
        this.startingAt = startingAt;
        this.endingAt = endingAt;
        this.interval = interval;
        this.time = time;
    }
}
