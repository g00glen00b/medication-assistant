package be.g00glen00b.apps.mediminder.user.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class ZoneIdConverterTest {
    private ZoneIdConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ZoneIdConverter();
    }

    @Test
    void convertToDatabaseColumn_mapsZoneId() {
        assertThat(converter.convertToDatabaseColumn(ZoneId.of("Australia/Brisbane"))).isEqualTo("Australia/Brisbane");
    }

    @Test
    void convertToEntityAttribute_mapsZoneId() {
        assertThat(converter.convertToEntityAttribute("Australia/Brisbane")).isEqualTo(ZoneId.of("Australia/Brisbane"));
    }
}