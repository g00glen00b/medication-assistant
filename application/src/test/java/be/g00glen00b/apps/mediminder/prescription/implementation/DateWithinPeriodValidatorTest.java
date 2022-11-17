package be.g00glen00b.apps.mediminder.prescription.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DateWithinPeriodValidatorTest {
    private static final ZonedDateTime TODAY = ZonedDateTime.of(2022, 11, 17, 8, 0, 0, 0, ZoneId.of("Europe/Brussels"));
    private DateWithinPeriodValidator validator;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(TODAY.toInstant(), TODAY.getZone());
        validator = new DateWithinPeriodValidator(clock);
    }

    @ParameterizedTest
    @CsvSource({
        "P1Y,2023-11-18,false",
        "P1Y,2023-11-17,true",
        "P2Y,2024-11-18,false",
        "P2Y,2024-11-17,true",
    })
    void validate_checksIfDateIsWithinRange(String period, String date, boolean expectedResult) {
        DateWithinPeriod annotation = mock(DateWithinPeriod.class);
        when(annotation.value()).thenReturn(period);
        validator.initialize(annotation);
        boolean result = validator.isValid(LocalDate.parse(date), null);
        assertThat(result).isEqualTo(expectedResult);
    }
}