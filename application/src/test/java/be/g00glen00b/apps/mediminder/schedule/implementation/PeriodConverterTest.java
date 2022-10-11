package be.g00glen00b.apps.mediminder.schedule.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

class PeriodConverterTest {
    private PeriodConverter converter;

    @BeforeEach
    void setUp() {
        converter = new PeriodConverter();
    }

    @Test
    void convertToDatabaseColumn_mapsPeriodToISOString() {
        assertThat(converter.convertToDatabaseColumn(Period.ofDays(1))).isEqualTo("P1D");
        assertThat(converter.convertToDatabaseColumn(Period.ofMonths(1))).isEqualTo("P1M");
    }

    @Test
    void convertToEntityAttribute_convertsISOStringToPeriod() {
        assertThat(converter.convertToEntityAttribute("P1D")).isEqualTo(Period.ofDays(1));
        assertThat(converter.convertToEntityAttribute("P1M")).isEqualTo(Period.ofMonths(1));
    }
}