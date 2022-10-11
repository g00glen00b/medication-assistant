package be.g00glen00b.apps.mediminder.schedule.implementation;

import be.g00glen00b.apps.mediminder.schedule.InvalidMedicationScheduleException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class MedicationSchedulePeriodTest {
    @Test
    void ofEndInclusive_returnsResult() {
        LocalDate startingAt = LocalDate.of(2022, 10, 10);
        LocalDate endingAtInclusive = LocalDate.of(2022, 10, 31);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofEndInclusive(startingAt, endingAtInclusive);
        assertThat(period)
            .usingRecursiveComparison()
            .isEqualTo(new MedicationSchedulePeriod(startingAt, endingAtInclusive));
    }

    @Test
    void ofEndInclusive_throwsErrorIfEndDateIsBeforeStartDate() {
        LocalDate startingAt = LocalDate.of(2022, 10, 10);
        LocalDate endingAtInclusive = LocalDate.of(2022, 10, 9);
        assertThatExceptionOfType(InvalidMedicationScheduleException.class)
            .isThrownBy(() -> MedicationSchedulePeriod.ofEndInclusive(startingAt, endingAtInclusive))
            .withMessage("End date has to go after the start date when given");
    }

    @Test
    void ofUnboundedEnd_returnsResult() {
        LocalDate startingAt = LocalDate.of(2022, 10, 10);
        MedicationSchedulePeriod period = MedicationSchedulePeriod.ofUnboundedEnd(startingAt);
        assertThat(period)
            .usingRecursiveComparison()
            .isEqualTo(new MedicationSchedulePeriod(startingAt, null));
    }
}