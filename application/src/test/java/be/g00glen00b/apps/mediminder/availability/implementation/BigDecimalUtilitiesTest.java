package be.g00glen00b.apps.mediminder.availability.implementation;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static be.g00glen00b.apps.mediminder.availability.implementation.BigDecimalUtilities.isPositive;
import static be.g00glen00b.apps.mediminder.availability.implementation.BigDecimalUtilities.toPercentage;
import static org.assertj.core.api.Assertions.assertThat;

class BigDecimalUtilitiesTest {
    @Test
    void isPositive_isTrueForPositiveNumbers() {
        assertThat(isPositive(BigDecimal.ZERO)).isFalse();
        assertThat(isPositive(BigDecimal.ONE)).isTrue();
        assertThat(isPositive(new BigDecimal("100"))).isTrue();
        assertThat(isPositive(new BigDecimal("0.001"))).isTrue();
        assertThat(isPositive(new BigDecimal("-1"))).isFalse();
    }

    @Test
    void toPercentage_dividesNumberByHundred() {
        assertThat(toPercentage(new BigDecimal("100"))).isEqualByComparingTo("1");
        assertThat(toPercentage(new BigDecimal("99"))).isEqualByComparingTo("0.99");
        assertThat(toPercentage(BigDecimal.ZERO)).isEqualByComparingTo("0");
    }
}