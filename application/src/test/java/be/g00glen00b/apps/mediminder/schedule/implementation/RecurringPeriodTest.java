package be.g00glen00b.apps.mediminder.schedule.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

class RecurringPeriodTest {
    @ParameterizedTest
    @CsvSource({
        "2022-01-08,true",
        "2022-01-07,false",
        "2022-01-09,false",
        "2022-02-08,false",
        "2022-01-08,true",
        "1899-01-08,false"
    })
    void isActiveOn_worksForYearlyPeriods(String date, boolean isActive) {
        RecurringPeriod period = new RecurringPeriod(LocalDate.of(1900, 1, 8), Period.ofYears(1));
        assertThat(period.isActiveOn(LocalDate.parse(date))).isEqualTo(isActive);
    }

    @ParameterizedTest
    @CsvSource({
        "2022-09-26,true",
        "2022-09-25,false",
        "2022-09-27,false",
        "2022-08-26,false",
        "1900-01-08,true",
        "1900-01-01,false"
    })
    void isActiveOn_worksForWeeklyPeriods(String date, boolean isActive) {
        RecurringPeriod period = new RecurringPeriod(LocalDate.of(1900, 1, 8), Period.ofWeeks(1));
        assertThat(period.isActiveOn(LocalDate.parse(date))).isEqualTo(isActive);
    }

    @Test
    void isActiveOn_worksForDailyPeriods() {
        RecurringPeriod period = new RecurringPeriod(LocalDate.of(1900, 1, 8), Period.ofDays(1));
        assertThat(period.isActiveOn(LocalDate.of(2022, 9, 26))).isTrue();
    }

    @Test
    void isActiveOn_worksForComplexPeriods() {
        RecurringPeriod period = new RecurringPeriod(LocalDate.of(1900, 1, 8), Period.of(1, 0, 1));
        assertThat(period.isActiveOn(LocalDate.of(2022, 5, 10))).isTrue();
    }

    @Test
    void isActiveOn_worksForLongComplexPeriods() {
        RecurringPeriod period = new RecurringPeriod(LocalDate.of(100, 1, 8), Period.of(0, 1, 0));
        assertThat(period.isActiveOn(LocalDate.of(3000, 5, 8))).isTrue();
    }
}