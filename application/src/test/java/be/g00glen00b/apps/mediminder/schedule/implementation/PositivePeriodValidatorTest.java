package be.g00glen00b.apps.mediminder.schedule.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

class PositivePeriodValidatorTest {
    private PositivePeriodValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PositivePeriodValidator();
    }

    @CsvSource({
        "P1D,true",
        "P1M,true",
        "P-1D,false",
        "P0D,false"
    })
    @ParameterizedTest
    void isValid_isTrueIfPeriodIsPositiveOrNonZero(String periodString, boolean isValid) {
        Period period = Period.parse(periodString);
        assertThat(validator.isValid(period, null)).isEqualTo(isValid);
    }

    @Test
    void isValid_isTrueIfNull() {
        assertThat(validator.isValid(null, null)).isTrue();
    }
}