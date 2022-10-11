package be.g00glen00b.apps.mediminder.schedule.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MedicationScheduleEntityTest {
    @Test
    void of_createsEntity() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(LocalDate.now());
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before lunch");
        assertThat(entity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationScheduleEntity(null, medicationId, userId, quantity, period, interval, time, "Before lunch", List.of(), null, null));
        assertThat(entity.getId()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
        "2022-01-01,true",
        "2022-01-10,true",
        "2022-01-31,true",
        "2022-02-01,false",
        "2021-12-31,false"
    })
    void isActiveOnDate_isTrueIfMedicationShouldBeTakenOnDate(String date, boolean isActive) {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.of(2022, 1, 1);
        LocalDate endingAtInclusive = LocalDate.of(2022, 1, 31);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofEndInclusive(startingAt, endingAtInclusive);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before lunch");
        assertThat(entity.isActiveOnDate(LocalDate.parse(date))).isEqualTo(isActive);
    }

    @ParameterizedTest
    @CsvSource({
        "2022-01-01,true",
        "2022-01-10,true",
        "2022-01-31,true",
        "2022-02-01,true",
        "2021-12-31,false"
    })
    void isActiveOnDate_isTrueIfMedicationShouldBeTakenOnDateForUnboundedEnd(String date, boolean isActive) {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.of(2022, 1, 1);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before lunch");
        assertThat(entity.isActiveOnDate(LocalDate.parse(date))).isEqualTo(isActive);
    }

    @Test
    void addCompletedEvent_addsEventToList() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.of(2022, 1, 1);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        LocalDate eventDate = LocalDate.of(2022, 1, 2);
        LocalDateTime completedDateTime = LocalDateTime.of(eventDate, LocalTime.of(8, 1));
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before lunch");
        MedicationScheduleCompletedEventEntity result = entity.addCompletedEvent(eventDate, completedDateTime);
        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationScheduleCompletedEventEntity(
                null,
                entity,
                LocalDateTime.of(eventDate, time),
                completedDateTime,
                null,
                null
            ));
        assertThat(result.getId()).isNotNull();
        assertThat(entity.getCompletedEvents()).contains(result);
    }

    @Test
    void addCompletedEvent_createsDomainEvent() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.of(2022, 1, 1);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        LocalDate eventDate = LocalDate.of(2022, 1, 2);
        LocalDateTime completedDateTime = LocalDateTime.of(eventDate, LocalTime.of(8, 1));
        MedicationScheduleEntity entity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, time, "Before lunch");
        entity.addCompletedEvent(eventDate, completedDateTime);
    }
}