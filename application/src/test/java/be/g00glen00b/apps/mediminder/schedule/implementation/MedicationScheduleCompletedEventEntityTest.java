package be.g00glen00b.apps.mediminder.schedule.implementation;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MedicationScheduleCompletedEventEntityTest {
    @Test
    void of_createsEntity() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(LocalDate.now());
        Period interval = Period.ofDays(1);
        LocalDateTime eventDate = LocalDateTime.of(2022, 10, 10, 8, 0);
        LocalDateTime completedDate = LocalDateTime.of(2022, 10, 10, 8, 1);
        MedicationScheduleEntity scheduleEntity = MedicationScheduleEntity.of(medicationId, userId, quantity, period, interval, eventDate.toLocalTime(), "After lunch");
        MedicationScheduleCompletedEventEntity entity = MedicationScheduleCompletedEventEntity.of(scheduleEntity, eventDate.toLocalDate(), completedDate);
        assertThat(entity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationScheduleCompletedEventEntity(
                null,
                scheduleEntity,
                eventDate,
                completedDate,
                null,
                null
            ));
        assertThat(entity.getId()).isNotNull();
    }
}